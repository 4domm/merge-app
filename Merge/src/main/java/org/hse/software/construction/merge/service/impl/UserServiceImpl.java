package org.hse.software.construction.merge.service.impl;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import org.hse.software.construction.merge.model.Meeting;
import org.hse.software.construction.merge.model.Role;
import org.hse.software.construction.merge.model.User;
import org.hse.software.construction.merge.repository.UserRepository;
import org.hse.software.construction.merge.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.security.Principal;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
@Service
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void createUser(User user) {
        user.setActive(true);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.getRoles().add(Role.USER);
        userRepository.save(user);
        log.info("saved new user with email: {}", userRepository.findByEmail(user.getEmail()));
    }

    @Override
    @Transactional
    public void changePassword(Principal principal, String newPassword, String oldPassword, String confirmPassword) {
        User user = getUserByPrincipal(principal);
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new RuntimeException("wrong password");
        }
        if (Objects.equals(oldPassword, newPassword)) {
            throw new RuntimeException("new password must be different from the old one");
        }
        if (!Objects.equals(confirmPassword, newPassword)) {
            throw new RuntimeException("you have incorrectly confirmed the password");
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        log.info("changed password");
    }

    @Override
    @Transactional
    public void changeUserData(Principal principal, User newUser) {
        User existingUser = getUserByPrincipal(principal);
        existingUser.setName(newUser.getName());
        existingUser.setSurname(newUser.getSurname());
    }

    @Override
    public User getUserByPrincipal(Principal principal) {
        return userRepository.findByEmail(principal.getName());
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
