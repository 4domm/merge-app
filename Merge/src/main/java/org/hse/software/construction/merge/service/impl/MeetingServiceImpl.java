package org.hse.software.construction.merge.service.impl;

import org.hse.software.construction.merge.model.Meeting;
import org.hse.software.construction.merge.repository.InvitationRepository;
import org.hse.software.construction.merge.repository.MeetingRepository;
import org.hse.software.construction.merge.service.MeetingService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class MeetingServiceImpl implements MeetingService {
    private final MeetingRepository meetingRepository;
    private final UserServiceImpl userService;
    private final InvitationRepository invitationRepository;

    @Override
    @Transactional
    public Meeting saveMeeting(Principal principal, Meeting meeting) {
        meeting.setCreator(userService.getUserByPrincipal(principal));
        return meetingRepository.save(meeting);
    }

    @Override
    public List<Meeting> findAllMeetingsCreatedByUser(Principal principal) {
        return meetingRepository.findMeetingsByCreator(userService.findByEmail(principal.getName()));
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
    public Meeting changeMeetingInfo(Meeting newMeeting, UUID oldMeetingId) {
        Meeting existingMeeting = findById(oldMeetingId);
        existingMeeting.setPlace(newMeeting.getPlace());
        existingMeeting.setDescription(newMeeting.getDescription());
        existingMeeting.setStartTime(newMeeting.getStartTime());
        existingMeeting.setEndTime(newMeeting.getEndTime());
        return meetingRepository.save(existingMeeting);
    }
    @Override
    @Transactional
    public Meeting updateMeeting(Meeting meeting) {
        return meetingRepository.save(meeting);
    }
    @Override
    public Meeting findById(UUID id) {
        return meetingRepository.findById(id).orElse(null);
    }
}
