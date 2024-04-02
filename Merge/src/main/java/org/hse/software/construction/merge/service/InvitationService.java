package org.hse.software.construction.merge.service;

import org.hse.software.construction.merge.model.Invitation;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

public interface InvitationService {
    List<Invitation> findAllInvitationsForCreator(Principal principal);

    List<Invitation> findAllInvitationsForInvitedUser(Principal principal);

    Invitation saveInvitation(Principal principal, Invitation invitation, String email, UUID meetingId);

    void acceptInvitation(UUID invitationId);

    Invitation update(Invitation invitation);

    void rejectInvite(UUID invitationId);

    void deleteById(UUID id);

    Invitation findById(UUID id);
}
