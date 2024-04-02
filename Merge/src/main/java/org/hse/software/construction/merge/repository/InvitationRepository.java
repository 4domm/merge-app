package org.hse.software.construction.merge.repository;

import org.hse.software.construction.merge.model.Invitation;
import org.hse.software.construction.merge.model.Meeting;
import org.hse.software.construction.merge.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface InvitationRepository extends JpaRepository<Invitation, UUID> {
    List<Invitation> findInvitationsByCreator(User creator);
    List<Invitation> findInvitationsByInvitedUser(User invitedUser);
    void deleteInvitationsByMeeting(Meeting meeting);
}
