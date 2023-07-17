package com.javalaunchpad.service;

import com.javalaunchpad.dto.request.PostSearchRequest;
import com.javalaunchpad.entity.Comment;
import com.javalaunchpad.entity.Post;
import com.javalaunchpad.entity.enumeration.PostStatus;
import com.javalaunchpad.exception.RessourceNotFoundException;
import org.springframework.data.domain.Page;

import java.security.Principal;
import java.util.List;
import java.util.Objects;

public interface PostService {
    Post createPost(Post post) throws RessourceNotFoundException;
    Post getPostById(Long postId) throws RessourceNotFoundException;
    Object getAllPosts();
    void deletePost(Long postId);
    Comment addCommentToPost(Long postId, Comment comment) throws RessourceNotFoundException ;
    List<Comment> getCommentsForPost(Long postId) throws RessourceNotFoundException ;
    Post updatePostStatus(Long postId, PostStatus status) throws RessourceNotFoundException ;
    List<Post> getPublishedPosts() ;
    Comment updateComment(Long commentId, String content) throws RessourceNotFoundException ;
    void deleteComment(Long commentId) throws RessourceNotFoundException ;
    Post updatePostContent(Long postId, String content) throws RessourceNotFoundException ;
    Post addImageToPost(Long postId, Long imageId) throws RessourceNotFoundException ;
    Page<Post> searchProduct(int i, int count, PostSearchRequest postSearchRequest, String sortBy, String sortOrder);
    // Other methods as per your requirements
}

