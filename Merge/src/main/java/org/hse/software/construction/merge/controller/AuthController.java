package org.hse.software.construction.merge.controller;

import org.hse.software.construction.merge.model.User;
import org.hse.software.construction.merge.service.impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/auth")
public class AuthController {
    private final UserServiceImpl userService;

    @GetMapping("/registration")
    public String registration() {
        return "registration";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/registration")
    public String signup(User user) {
        User existing_user = userService.findByEmail(user.getEmail());
        if (existing_user != null) {
            return "login";
        }
        userService.createUser(user);
        log.info("user registered");
        return "redirect:/auth/login";
    }

}
