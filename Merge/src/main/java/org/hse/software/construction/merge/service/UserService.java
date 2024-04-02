package org.hse.software.construction.merge.service;

import org.hse.software.construction.merge.model.User;

import java.security.Principal;

public interface UserService {
    void createUser(User user);

    User getUserByPrincipal(Principal principal);

    User findByEmail(String email);
}
