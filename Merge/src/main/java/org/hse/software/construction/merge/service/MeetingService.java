package org.hse.software.construction.merge.service;

import org.hse.software.construction.merge.model.Meeting;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

public interface MeetingService {
    Meeting saveMeeting(Principal principal, Meeting meeting);

    List<Meeting> findAllMeetingsCreatedByUser(Principal principal);

    List<Meeting> findAllMeetingsWithUser(Principal principal);

    void deleteById(UUID id);

    Meeting changeMeetingInfo(Meeting newMeeting, UUID oldMeetingId);

    Meeting updateMeeting(Meeting meeting);
    void deleteMeetingWithInvitations(UUID id);
    Meeting findById(UUID id);
}
