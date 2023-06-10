package com.javalaunchpad.repository;

import com.javalaunchpad.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
    // Custom query methods or additional operations can be defined here
}

