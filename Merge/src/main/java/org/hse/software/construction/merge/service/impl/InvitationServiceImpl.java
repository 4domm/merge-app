package org.hse.software.construction.merge.service.impl;

import org.hse.software.construction.merge.model.Invitation;
import org.hse.software.construction.merge.model.Meeting;
import org.hse.software.construction.merge.model.Status;
import org.hse.software.construction.merge.model.User;
import org.hse.software.construction.merge.repository.InvitationRepository;
import org.hse.software.construction.merge.service.InvitationService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class InvitationServiceImpl implements InvitationService {
    private final InvitationRepository repository;
    private final UserServiceImpl userService;
    private final MeetingServiceImpl meetingService;
    @Override
    public List<Invitation> findAllInvitationsForCreator(Principal principal) {
        List<Invitation> invitationList = repository.findInvitationsByCreator(userService.findByEmail(principal.getName()));
        if (invitationList == null) {
            return new ArrayList<>();
        }
        return invitationList;
    }
    @Override
    public List<Invitation> findAllInvitationsForInvitedUser(Principal principal) {
        List<Invitation> invitationList = repository.findInvitationsByInvitedUser(userService.findByEmail(principal.getName()));
        if (invitationList == null) {
            return new ArrayList<>();
        }
        return invitationList;
    }
    @Override
    @Transactional
    public Invitation saveInvitation(Principal principal, @ModelAttribute Invitation invitation, @RequestParam String email, @RequestParam UUID meetingId) {
        User user = userService.findByEmail(email);
        Meeting meeting = meetingService.findById(meetingId);
        if (user == null) {
            throw new NullPointerException("no user with this email");
        }
        invitation.setInvitedUser(user);
        invitation.setMeeting(meeting);
        invitation.setCreator(userService.getUserByPrincipal(principal));
        return repository.save(invitation);
    }
    @Override
    @Transactional
    public void acceptInvitation(UUID invitationId) {
        Invitation invitation = findById(invitationId);
        if (invitation.getStatus().equals(Status.WAITING)) {
            invitation.getMeeting().getMembers().add(invitation.getInvitedUser());
            meetingService.updateMeeting(invitation.getMeeting());
            invitation.setStatus(Status.ACCEPTED);
            update(invitation);
        }
    }

    @Override
    @Transactional
    public Invitation update(Invitation invitation) {
        return repository.save(invitation);
    }
    @Override
    @Transactional
    public void rejectInvite(UUID invitationId) {
        Invitation invitation = findById(invitationId);
        if (invitation.getStatus().equals(Status.WAITING)) {
            invitation.getMeeting().getMembers().remove(invitation.getInvitedUser());
            meetingService.updateMeeting(invitation.getMeeting());
            invitation.setStatus(Status.REJECTED);
            update(invitation);
        }
    }
    @Override
    @Transactional
    public void deleteById(UUID id) {
        repository.deleteById(id);
    }
    @Override
    public Invitation findById(UUID id) {
        return repository.findById(id).orElse(null);
    }

}
