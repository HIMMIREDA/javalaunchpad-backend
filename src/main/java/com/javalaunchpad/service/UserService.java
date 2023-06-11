package com.javalaunchpad.service;


import com.javalaunchpad.exception.RessourceNotFoundException;
import com.javalaunchpad.security.entity.User;

import java.util.List;

public interface UserService {
    User createUser(User user);
    User getUserById(Long userId) throws RessourceNotFoundException;
    List<User> getAllUsers();
    void deleteUser(Long userId);
    // Other methods as per your requirements
}

