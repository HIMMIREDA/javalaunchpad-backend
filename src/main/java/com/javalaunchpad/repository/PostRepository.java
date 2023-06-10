package com.javalaunchpad.repository;

import com.javalaunchpad.entity.Post;
import com.javalaunchpad.entity.enumeration.PostStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>{
    List<Post> findByStatus(PostStatus status);
    List<Post> findByTitleContainingIgnoreCase(String title);
    // Custom query methods or additional operations can be defined here
}

