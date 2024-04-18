package org.hse.software.construction.merge.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.hse.software.construction.merge.model.Meeting;
import org.hse.software.construction.merge.model.User;
import org.hse.software.construction.merge.repository.InvitationRepository;
import org.hse.software.construction.merge.repository.MeetingRepository;
import org.hse.software.construction.merge.service.MeetingService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
public class MeetingServiceImpl implements MeetingService {
    private final MeetingRepository meetingRepository;
    private final UserServiceImpl userService;
    private final InvitationRepository invitationRepository;
    private final EmailService emailService;

    @Override
    @Transactional
    public Meeting saveMeeting(Principal principal, Meeting meeting) {
        if (!meeting.getStartTime().isBefore(meeting.getEndTime())) {
            throw new IllegalArgumentException("wrong start time and end time");
        }
        meeting.setCreator(userService.getUserByPrincipal(principal));
        return meetingRepository.save(meeting);
    }

    @Override
    public List<Meeting> findAllMeetingsCreatedByUser(Principal principal) {
        return meetingRepository.findMeetingsByCreator(userService.findByEmail(principal.getName()));
    }

    @Override
    @Transactional
    public void sendNotification(UUID id) {
        Meeting meeting = findById(id);
        if (LocalDateTime.now().isBefore(meeting.getStartTime()) && !meeting.isNotified()) {
            log.info("sending noti");
            for (User user : meeting.getMembers()) {
                LocalDateTime now = LocalDateTime.now();
                LocalDateTime startTime = meeting.getStartTime();
                Duration duration = Duration.between(now, startTime);
                long hoursDiff = duration.toHours();
                long minutesDiff = duration.toMinutesPart();
                String creatorName = meeting.getCreator().getName();
                String notificationMessage = String.format(
                        "Уведомление: ваша встреча с %s начнется через %d часов и %d минут",
                        creatorName, hoursDiff, minutesDiff
                );
                emailService.sendEmail(user.getEmail(), "Уведомление о встрече", notificationMessage);
                meeting.setNotified(true);
                meetingRepository.save(meeting);
            }
        }
    }

    @Override
    public List<Meeting> findAllMeetingsWithUser(Principal principal) {
        return meetingRepository.findMeetingsByMembersContaining(userService.findByEmail(principal.getName()));
    }

    @Override
    @Transactional
    public void deleteMeetingWithInvitations(UUID id) {
        Meeting meeting = findById(id);
        invitationRepository.deleteInvitationsByMeeting(meeting);
        deleteById(id);
    }

    @Override
    @Transactional
    public void deleteById(UUID id) {
        meetingRepository.deleteById(id);
    }

    @Override
    @Transactional
    public Meeting changeMeetingInfo(Meeting newMeeting, UUID oldMeetingId, Principal principal) {
        Meeting existingMeeting = findById(oldMeetingId);
        if (existingMeeting.getCreator() != userService.getUserByPrincipal(principal)) {
            throw new RuntimeException("you cant update this meeting");
        }
        existingMeeting.setPlace(newMeeting.getPlace());
        existingMeeting.setDescription(newMeeting.getDescription());
        existingMeeting.setStartTime(newMeeting.getStartTime());
        existingMeeting.setEndTime(newMeeting.getEndTime());
        if (!existingMeeting.getStartTime().isBefore(existingMeeting.getEndTime())) {
            throw new IllegalArgumentException("wrong start time and end time");
        }
        return meetingRepository.save(existingMeeting);
    }

    @Override
    @Transactional
    public Meeting updateMeeting(Meeting meeting) {
        return meetingRepository.save(meeting);
    }

    @Override
    @Transactional
    public Meeting findById(UUID id) {
        return meetingRepository.findById(id).orElse(null);
    }
}
