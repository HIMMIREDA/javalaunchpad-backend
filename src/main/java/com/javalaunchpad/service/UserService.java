package com.javalaunchpad.service;


import com.javalaunchpad.entity.User;
import com.javalaunchpad.exception.RessourceNotFoundException;

import java.util.List;

public interface UserService {
    User createUser(User user);
    User getUserById(Long userId) throws RessourceNotFoundException;
    List<User> getAllUsers();
    void deleteUser(Long userId);
    // Other methods as per your requirements
}

