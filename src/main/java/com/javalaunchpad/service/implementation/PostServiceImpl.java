package com.javalaunchpad.service.implementation;

import com.javalaunchpad.entity.Comment;
import com.javalaunchpad.entity.Image;
import com.javalaunchpad.entity.Post;
import com.javalaunchpad.entity.enumeration.PostStatus;
import com.javalaunchpad.exception.RessourceNotFoundException;
import com.javalaunchpad.mapper.PostMapper;
import com.javalaunchpad.repository.CommentRepository;
import com.javalaunchpad.repository.ImageRepository;
import com.javalaunchpad.repository.PostRepository;
import com.javalaunchpad.service.PostService;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

import static com.javalaunchpad.utils.AuthorizationUtils.isAdmin;

@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository ;

    private final ImageRepository imageRepository ;

    private final PostMapper postMapper ;

    public PostServiceImpl(PostRepository postRepository , CommentRepository commentRepository , ImageRepository imageRepository , PostMapper postMapper ) {
        this.imageRepository = imageRepository;
        this.commentRepository = commentRepository ;
        this.postRepository = postRepository;
        this.postMapper = postMapper ;
    }

    @Override
    public Post createPost(Post post) {
        // Implement the logic to create a post
        post.setStatus(PostStatus.DRAFT); // Set the initial status as draft
        return postRepository.save(post);
    }

    public Post updatePostContent(Long postId, String content) throws RessourceNotFoundException {
        Post post = getPostById(postId);
        post.setContent(content);
        return postRepository.save(post);
    }

    public Post updatePostStatus(Long postId, PostStatus status) throws RessourceNotFoundException {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RessourceNotFoundException("Post not found"));
        post.setStatus(status);
        return postRepository.save(post);
    }

    public List<Post> getPublishedPosts() {
        return postRepository.findByStatus(PostStatus.PUBLISHED);
    }

    @Override
    public List<Post> searchPosts(String keyword, Long categoryId, Long tagId) {
        return null;
    }

    @Override
    public Post getPostById(Long postId) throws RessourceNotFoundException {
        // Implement the logic to retrieve a post by ID
        return postRepository.findById(postId)
                .orElseThrow(() -> new RessourceNotFoundException("Post not found"));
    }

    @Override
    public Object getAllPosts(Principal principal) {
        // Implement the logic to retrieve all posts
        List<Post> allPosts = postRepository.findAll();
        if (isAdmin(principal)){
            return  postMapper.toPostAdminResponseList(allPosts);
        }
        return allPosts ;
    }

    @Override
    public void deletePost(Long postId) {
        // Implement the logic to delete a post
        postRepository.deleteById(postId);
    }

    public Comment addCommentToPost(Long postId, Comment comment) throws RessourceNotFoundException {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RessourceNotFoundException("Post not found with id: " + postId));

        // Associate the comment with the post
        comment.setPost(post);
        Comment savedComment = commentRepository.save(comment);

        // Update the post's comment count
        post.setCommentCount(post.getCommentCount() + 1);
        postRepository.save(post);

        return savedComment;
    }


    public List<Comment> getCommentsForPost(Long postId) throws RessourceNotFoundException {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RessourceNotFoundException("Post not found with id: " + postId));

        return commentRepository.findByPost(post);
    }
    public void deleteComment(Long commentId) throws RessourceNotFoundException {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RessourceNotFoundException("Comment not found with id: " + commentId));

        // Decrement the post's comment count
        Post post = comment.getPost();
        post.setCommentCount(post.getCommentCount() - 1);
        postRepository.save(post);

        commentRepository.delete(comment);
    }
    public Comment updateComment(Long commentId, String content) throws RessourceNotFoundException {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RessourceNotFoundException("Comment not found with id: " + commentId));

        comment.setContent(content);
        return commentRepository.save(comment);
    }
 /*   public List<Post> searchPosts(String keyword, Long categoryId, Long tagId) {
        Specification<Post> specification = Specification.where(null);

        // Add search criteria based on the query parameter
        specification = specification.and((root, query, criteriaBuilder) ->
                criteriaBuilder.or(
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("title")),
                                "%" + keyword.toLowerCase() + "%"),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("content")),
                                "%" + keyword.toLowerCase() + "%")
                ));

        // Add category filter if provided
        if (categoryId != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.join("categories").get("id"), categoryId));
        }

        // Add tag filter if provided
        if (tagId != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.join("tags").get("id"), tagId));
        }

        return postRepository.findAll(specification);
    }*/


    @Override
    public Post addImageToPost(Long postId, Long imageId) throws RessourceNotFoundException {
        Post post = getPostById(postId);
        Image image = imageRepository.findById(imageId).orElseThrow(() -> new RessourceNotFoundException("no image with this id"));
        post.addImage(image);
        return postRepository.save(post);
    }

    // Other methods as per your requirements
}

