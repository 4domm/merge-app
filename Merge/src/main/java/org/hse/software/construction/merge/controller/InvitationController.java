package org.hse.software.construction.merge.controller;

import org.hse.software.construction.merge.model.Invitation;
import org.hse.software.construction.merge.service.impl.InvitationServiceImpl;
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
public class InvitationController {
    private final InvitationServiceImpl invitationService;
    private final MeetingServiceImpl meetingService;

    @GetMapping("/invitation")
    public String invitations(Principal principal, Model model) {
        model.addAttribute("invitations", invitationService.findAllInvitationsForInvitedUser(principal));
        model.addAttribute("sentInvitations", invitationService.findAllInvitationsForCreator(principal));
        return "invitations";
    }

    @GetMapping("/invitation/create")
    public String showMeetings(Principal principal, Model model) {
        model.addAttribute("meetings", meetingService.findAllMeetingsCreatedByUser(principal));
        return "create-invitation-form";
    }

    @PostMapping("/invitation/create")
    public String sentInvitations(Principal principal, @ModelAttribute("invitation") Invitation invitation, @RequestParam String email, @RequestParam UUID meetingId, BindingResult result) {
        if (result.hasErrors()) {
            throw new RuntimeException("Wrong data input");
        }
        invitationService.saveInvitation(principal, invitation, email, meetingId);
        return "redirect:/invitation";
    }

    @PostMapping("/invitation/accept/{id}")
    public String acceptInvitations(@PathVariable UUID id) {
        invitationService.acceptInvitation(id);
        return "redirect:/invitation";
    }

    @PostMapping("/invitation/reject/{id}")
    public String rejectInvitations(@PathVariable UUID id) {
        invitationService.rejectInvite(id);
        return "redirect:/invitation";
    }


}
