package org.hse.software.construction.merge.controller;

import org.hse.software.construction.merge.model.Meeting;
import org.hse.software.construction.merge.service.impl.MeetingServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.UUID;

@RequiredArgsConstructor
@Controller
@Slf4j
public class MeetingController {
    private final MeetingServiceImpl meetingService;

    @GetMapping("/")
    public String meetings(Principal principal, Model model) {
        model.addAttribute("meetings", meetingService.findAllMeetingsCreatedByUser(principal));
        model.addAttribute("invmeetings", meetingService.findAllMeetingsWithUser(principal));
        return "meetings";
    }

    @GetMapping("/meeting/{id}")
    public String meetingInfo(@PathVariable UUID id, Model model) {
        model.addAttribute("meeting", meetingService.findById(id));
        return "meeting-info";
    }

    @GetMapping("/meeting/create")
    public String showCreateMeetingForm() {
        return "create-form";
    }

    @PostMapping("/meeting/create")
    public String createMeeting(Principal principal, @ModelAttribute("meeting") Meeting meeting, BindingResult result) {
        if (result.hasErrors()) {
            throw new RuntimeException("Wrong data input");
        }
        meetingService.saveMeeting(principal, meeting);
        return "redirect:/";
    }


    @PostMapping("/meeting/delete/{id}")
    public String deleteMeeting(@PathVariable UUID id) {
        meetingService.deleteMeetingWithInvitations(id);
        return "redirect:/";
    }

    @GetMapping("/meeting/update/{id}")
    public String showUpdateForm(@PathVariable UUID id, Model model) {
        Meeting meeting = meetingService.findById(id);
        model.addAttribute("meeting", meeting);
        return "update-meeting-form";
    }

    @PostMapping("/meeting/update/{id}")
    public String updateMeeting(@PathVariable UUID id, @ModelAttribute("meeting") Meeting meeting, BindingResult result) {
        if (result.hasErrors()) {
            throw new RuntimeException("Wrong data input");
        }
        meetingService.changeMeetingInfo(meeting, id);
        return "redirect:/";
    }

}
