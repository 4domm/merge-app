package org.hse.software.construction.merge.service.impl;

import jakarta.transaction.Transactional;
import org.hse.software.construction.merge.model.Role;
import org.hse.software.construction.merge.model.User;
import org.hse.software.construction.merge.repository.UserRepository;
import org.hse.software.construction.merge.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;

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
    public User getUserByPrincipal(Principal principal) {
        return userRepository.findByEmail(principal.getName());
    }
    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
