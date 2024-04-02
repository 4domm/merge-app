package org.hse.software.construction.merge.service;

import org.hse.software.construction.merge.model.User;

import java.security.Principal;
import java.util.Optional;
import java.util.UUID;

public interface UserService {
    void createUser(User user);

    User getUserByPrincipal(Principal principal);

    User findByEmail(String email);

    void changePassword(Principal principal, String newPassword, String oldPassword, String confirmPassword);

    void changeUserData(Principal principal, User newUser);
}
