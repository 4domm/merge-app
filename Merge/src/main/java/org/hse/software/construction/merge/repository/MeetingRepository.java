package org.hse.software.construction.merge.repository;

import org.hse.software.construction.merge.model.Meeting;
import org.hse.software.construction.merge.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface MeetingRepository extends JpaRepository<Meeting, UUID> {
    List<Meeting> findMeetingsByCreator(User creator);

    List<Meeting> findMeetingsByMembersContaining(User invitedUser);
}
