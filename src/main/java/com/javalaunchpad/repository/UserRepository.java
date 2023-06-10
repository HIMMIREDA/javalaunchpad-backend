package com.javalaunchpad.repository;

import com.javalaunchpad.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Custom query methods or additional operations can be defined here
}

