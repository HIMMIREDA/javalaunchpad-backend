package com.javalaunchpad.service.implementation;

import com.javalaunchpad.entity.Comment;
import com.javalaunchpad.exception.RessourceNotFoundException;
import com.javalaunchpad.repository.CommentRepository;
import com.javalaunchpad.service.CommentService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    public CommentServiceImpl(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Override
    public Comment createComment(Comment comment) {
        // Implement the logic to create a comment
        return commentRepository.save(comment);
    }

    @Override
    public Comment getCommentById(Long commentId) throws RessourceNotFoundException {
        // Implement the logic to retrieve a comment by ID
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new RessourceNotFoundException("Comment not found"));
    }

    @Override
    public List<Comment> getAllComments() {
        // Implement the logic to retrieve all comments
        return commentRepository.findAll();
    }

    @Override
    public void deleteComment(Long commentId) {
        // Implement the logic to delete a comment
        commentRepository.deleteById(commentId);
    }

    // Other methods as per your requirements
}
