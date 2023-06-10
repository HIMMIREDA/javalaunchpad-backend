package com.javalaunchpad.repository;

import com.javalaunchpad.entity.Comment;
import com.javalaunchpad.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPost(Post post);
    // Custom query methods or additional operations can be defined here
}
