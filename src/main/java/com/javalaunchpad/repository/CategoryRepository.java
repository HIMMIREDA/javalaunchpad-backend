package com.javalaunchpad.repository;

import com.javalaunchpad.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    // Custom query methods or additional operations can be defined here
}

