package com.javalaunchpad.web;

import com.javalaunchpad.dto.request.UpdatePostContentRequest;
import com.javalaunchpad.entity.Comment;
import com.javalaunchpad.entity.Post;
import com.javalaunchpad.entity.Tag;
import com.javalaunchpad.entity.enumeration.PostStatus;
import com.javalaunchpad.exception.RessourceNotFoundException;
import com.javalaunchpad.repository.PostRepository;
import com.javalaunchpad.service.ImageStorageService;
import com.javalaunchpad.service.PostService;
import com.javalaunchpad.service.TagService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/posts")
@AllArgsConstructor
@CrossOrigin
public class PostController {

    private final PostService postService;
    private final ImageStorageService imageStorageService ;
    private final TagService tagService ;
    private final PostRepository postRepository ;

    // this method will be used for update also (if id != null hibernate will only update not create)
    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Post> createPost(@RequestBody Post post) throws RessourceNotFoundException {
        Post createdPost = postService.createPost(post);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPost);
    }
    // tested
    @PutMapping("/{postId}/status")
    public ResponseEntity<Post> updatePostStatus(
            @PathVariable Long postId,
            @RequestParam PostStatus status) throws RessourceNotFoundException {
        Post updatedPost = postService.updatePostStatus(postId, status);
        return ResponseEntity.ok(updatedPost);
    }
     // tested
    @GetMapping("/published")
    public ResponseEntity<List<Post>> getPublishedPosts() {
        List<Post> publishedPosts = postService.getPublishedPosts();
        return ResponseEntity.ok(publishedPosts);
    }
     // tested
    @GetMapping("/{postId}")
    public ResponseEntity<Post> getPostById(@PathVariable Long postId) throws RessourceNotFoundException {
        Post post = postService.getPostById(postId);
        return ResponseEntity.ok(post);
    }

    // tested
    @GetMapping
    public ResponseEntity< Object> getAllPosts(Principal principal) {
        Object posts = postService.getAllPosts(principal);
        return ResponseEntity.ok(posts);
    }

    // tested
    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable Long postId) {
        postService.deletePost(postId);
        return ResponseEntity.noContent().build();
    }

    // Comment endpoints
    @PostMapping("/{postId}/comments")
    public ResponseEntity<Comment> addCommentToPost(@PathVariable Long postId, @RequestBody Comment comment) throws RessourceNotFoundException {
        Comment addedComment = postService.addCommentToPost(postId, comment);
        return ResponseEntity.status(HttpStatus.CREATED).body(addedComment);
    }

    @GetMapping("/{postId}/comments")
    public ResponseEntity<List<Comment>> getCommentsForPost(@PathVariable Long postId) throws RessourceNotFoundException {
        List<Comment> comments = postService.getCommentsForPost(postId);
        return ResponseEntity.ok(comments);
    }

    @PutMapping("/{postId}/content")
    public ResponseEntity<Post> updatePostContent(@PathVariable Long postId, @RequestBody UpdatePostContentRequest request) {
        try {
            Post updatedPost = postService.updatePostContent(postId, request.getContent());
            return ResponseEntity.ok(updatedPost);
        } catch (RessourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Endpoint to search posts
    @GetMapping("/search")
    public List<Post> searchPosts(@RequestParam(name = "keyword") String keyword,
                                  @RequestParam(name = "category", required = false) Long categoryId,
                                  @RequestParam(name = "tag", required = false) Long tagId) {
        return postService.searchPosts(keyword , categoryId , tagId);
    }

    @PostMapping("/{postId}/images")
    public ResponseEntity<String> uploadImage(@PathVariable Long postId,
                                              @RequestParam("file") MultipartFile file) {
        try {
            // Store the image and get the image path
            Long imageId = imageStorageService.storeImage(file, postId);

            // Update the post with the image path
           postService.addImageToPost(postId, imageId);

            return ResponseEntity.ok("Image uploaded successfully");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to upload image");
        } catch (RessourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
    // Other endpoints as per your requirements


    @PostMapping("/{postId}/tags/{tagId}")
    public void assignTagToPost(@PathVariable Long postId, @PathVariable Long tagId) throws RessourceNotFoundException {
        // Fetch the Post and Tag objects from the database using their respective IDs
        Post post = postService.getPostById(postId);
        Tag tag = tagService.getTagById(tagId);
        // Assign the Tag to the Post
        post.getTags().add(tag);
        // Save the changes
        postRepository.save(post);
    }

}

