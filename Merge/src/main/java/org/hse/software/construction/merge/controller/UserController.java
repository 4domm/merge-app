package org.hse.software.construction.merge.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hse.software.construction.merge.model.Meeting;
import org.hse.software.construction.merge.model.User;
import org.hse.software.construction.merge.service.impl.UserServiceImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.UUID;

@RequiredArgsConstructor
@Controller
@Slf4j
@RequestMapping("/user")
public class UserController {
    private final UserServiceImpl userService;

    @GetMapping("/profile")
    public String profileUser(Principal principal, Model model) {
        model.addAttribute("user", userService.getUserByPrincipal(principal));
        return "user-info";
    }

    @GetMapping("/change-password")
    public String showPasswordForm(Principal principal, Model model) {
        model.addAttribute("user", userService.getUserByPrincipal(principal));
        return "update-password-form";
    }

    @PostMapping("/change-password")
    public String changePassword(@ModelAttribute("user") User user,
                                 @RequestParam("newPassword") String newPassword,
                                 @RequestParam("oldPassword") String oldPassword,
                                 @RequestParam("confirmPassword") String confirmPassword,
                                 BindingResult result,
                                 Principal principal) {
        if (result.hasErrors()) {
            throw new RuntimeException("Wrong data input");
        }
        userService.changePassword(principal, newPassword, oldPassword,confirmPassword);
        return "redirect:/user/profile";
    }

    @GetMapping("/change-data")
    public String showDataForm(Principal principal, Model model) {
        model.addAttribute("user", userService.getUserByPrincipal(principal));
        return "update-user-data-form";
    }

    @PostMapping("/change-data")
    public String changeUser(Principal principal, @ModelAttribute("user") User user, BindingResult result) {
        if (result.hasErrors()) {
            throw new RuntimeException("Wrong data input");
        }
        userService.changeUserData(principal, user);
        return "redirect:/user/profile";
    }


}
