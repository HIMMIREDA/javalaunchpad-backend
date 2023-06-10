package com.javalaunchpad.service;

import com.javalaunchpad.entity.Comment;
import com.javalaunchpad.exception.RessourceNotFoundException;

import java.util.List;

public interface CommentService {
    Comment createComment(Comment comment);
    Comment getCommentById(Long commentId) throws RessourceNotFoundException;
    List<Comment> getAllComments();
    void deleteComment(Long commentId);
    // Other methods as per your requirements
}

