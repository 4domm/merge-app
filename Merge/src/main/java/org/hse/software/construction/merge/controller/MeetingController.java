package org.hse.software.construction.merge.controller;

import org.hibernate.Hibernate;
import org.hse.software.construction.merge.model.Meeting;
import org.hse.software.construction.merge.service.impl.MeetingServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.UUID;

@RequiredArgsConstructor
@Controller
@Slf4j
@RequestMapping("/meeting")
public class MeetingController {
    private final MeetingServiceImpl meetingService;

    @GetMapping("")
    public String meetings(Principal principal, Model model) {
        model.addAttribute("meetings", meetingService.findAllMeetingsCreatedByUser(principal));
        model.addAttribute("invmeetings", meetingService.findAllMeetingsWithUser(principal));
        return "meetings";
    }

    @GetMapping("/{id}")
    public String meetingInfo(@PathVariable UUID id, Model model) {
        model.addAttribute("meeting", meetingService.findById(id));
        return "meeting-info";
    }

    @PostMapping("/notify/{id}")
    public String notifyUsers(@PathVariable UUID id) {
        meetingService.sendNotification(id);
        return "redirect:/meeting";
    }

    @GetMapping("/create")
    public String showCreateMeetingForm() {
        return "create-form";
    }

    @PostMapping("/create")
    public String createMeeting(Principal principal, @ModelAttribute("meeting") Meeting meeting, BindingResult result) {
        if (result.hasErrors()) {
            throw new RuntimeException("Wrong data input");
        }
        meetingService.saveMeeting(principal, meeting);
        return "redirect:/meeting";
    }


    @PostMapping("/delete/{id}")
    public String deleteMeeting(@PathVariable UUID id) {
        meetingService.deleteMeetingWithInvitations(id);
        return "redirect:/meeting";
    }

    @GetMapping("/update/{id}")
    public String showUpdateForm(@PathVariable UUID id, Model model) {
        Meeting meeting = meetingService.findById(id);
        model.addAttribute("meeting", meeting);
        return "update-meeting-form";
    }

    @PostMapping("/update/{id}")
    public String updateMeeting(@PathVariable UUID id, @ModelAttribute("meeting") Meeting meeting, BindingResult result, Principal principal) {
        if (result.hasErrors()) {
            throw new RuntimeException("Wrong data input");
        }
        meetingService.changeMeetingInfo(meeting, id, principal);
        return "redirect:/meeting";
    }

}
