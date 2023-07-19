package com.javalaunchpad.service.implementation;

import com.javalaunchpad.dto.request.PostSearchRequest;
import com.javalaunchpad.entity.Comment;
import com.javalaunchpad.entity.Image;
import com.javalaunchpad.entity.Post;
import com.javalaunchpad.entity.enumeration.PostStatus;
import com.javalaunchpad.exception.RessourceNotFoundException;
import com.javalaunchpad.mapper.PostMapper;
import com.javalaunchpad.repository.CommentRepository;
import com.javalaunchpad.repository.ImageRepository;
import com.javalaunchpad.repository.PostRepository;
import com.javalaunchpad.search.SearchCriteria;
import com.javalaunchpad.search.post.PostSpecificationBuilder;
import com.javalaunchpad.security.User;
import com.javalaunchpad.service.PostService;
import com.javalaunchpad.service.UserService;
import com.javalaunchpad.utils.AuthorizationUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Service
public class PostServiceImpl implements PostService {

    static private final List<String> sortByFieldsList = Arrays.asList(
            "publicationDate",
            "lastUpdate",
            "price",
            "commentCount",
            "views"
    );
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final ImageRepository imageRepository;
    private final UserService userService;
    private final PostMapper postMapper;

    public PostServiceImpl(PostRepository postRepository, CommentRepository commentRepository, ImageRepository imageRepository, PostMapper postMapper, UserService userService) {
        this.imageRepository = imageRepository;
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.postMapper = postMapper;
        this.userService = userService;
    }

    @Override
    public Post createPost(Post post) throws RessourceNotFoundException {
        User user = getUser();
        post.setAuthor(user);
        post.setLastUpdate(LocalDateTime.now());
        // Implement the logic to create a post
        post.setStatus(PostStatus.DRAFT); // Set the initial status as draft
        return postRepository.save(post);
    }

    private User getUser() throws RessourceNotFoundException {
        // get the Authenticated User from the SecurityContext
        User user = null;
        String principal = SecurityContextHolder.getContext().getAuthentication().getName();
        user = userService.getUserByEmail(principal);
        return user;
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
    public Post getPostById(Long postId) throws RessourceNotFoundException {
        // Implement the logic to retrieve a post by ID
        return postRepository.findById(postId)
                .orElseThrow(() -> new RessourceNotFoundException("Post not found"));
    }

    @Override
    public Object getAllPosts() {
        if (AuthorizationUtils.isSuperAdmin()) {
            // mapping to PostSuperAdminResponse
            return postRepository.findAll().stream().map(postMapper::toPostSuperAdminResponse).toList();
        }
        // Implement the logic to retrieve all posts
        return postRepository.findAll();
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

    @Override
    public Page<Post> searchPosts(int numPage, int pageCount, PostSearchRequest postSearchRequest, String sortBy, String sortOrder) {
        PostSpecificationBuilder builder = new PostSpecificationBuilder();
        if (postSearchRequest != null) {
            List<SearchCriteria> criteriaList = postSearchRequest.getSearchCriteriaList();
            if (criteriaList != null) {
                criteriaList.forEach(x -> {
                    x.setDataOption(postSearchRequest.getDataOption());
                    builder.with(x);
                });
            }
        }
        Sort sortCriteria = Sort.by(Sort.Direction.DESC, "publicationDate");
        if (sortByFieldsList.contains(sortBy)) {
            sortCriteria = Sort.by((sortOrder != null && sortOrder.equalsIgnoreCase("ASC")) ? Sort.Direction.ASC : Sort.Direction.DESC, sortBy);
        }
        Pageable paging = PageRequest.of(numPage, pageCount, sortCriteria);
        return postSearchRequest == null ?
                postRepository.findAll(paging) :
                postRepository.findAll(builder.build(), paging);
    }

    // Other methods as per your requirements
}

