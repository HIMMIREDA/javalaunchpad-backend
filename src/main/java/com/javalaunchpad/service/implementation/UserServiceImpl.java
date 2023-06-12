package com.javalaunchpad.service.implementation;

import com.javalaunchpad.exception.RessourceNotFoundException;
import com.javalaunchpad.security.entity.User;
import com.javalaunchpad.security.repo.UserRepository;
import com.javalaunchpad.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User createUser(User user) {
        // Implement the logic to create a user
        return userRepository.save(user);
    }

    @Override
    public User getUserById(Long userId) throws RessourceNotFoundException {
        // Implement the logic to retrieve a user by ID
        return userRepository.findById(userId)
                .orElseThrow(() -> new RessourceNotFoundException("User not found"));
    }

    @Override
    public List<User> getAllUsers() {
        // Implement the logic to retrieve all users
        return userRepository.findAll();
    }

    @Override
    public void deleteUser(Long userId) {
        // Implement the logic to delete a user
        userRepository.deleteById(userId);
    }

    @Override
    public User getUserByEmail(String id) throws RessourceNotFoundException {
      return  userRepository.findByUsername(id).orElseThrow(() -> new RessourceNotFoundException("User not found"));
    }

    // Other methods as per your requirements
}

