package com.javalaunchpad.entity;

import com.javalaunchpad.entity.enumeration.PostStatus;
import com.javalaunchpad.security.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    // to store a short description about the Post that will be displayed in the search page .
    @Column(columnDefinition = "TEXT" , nullable = false)
    private String excerpt ;

    @Column(nullable = true)
    private LocalDateTime publicationDate;

    @Column(nullable = true)
    private LocalDateTime draftCreationDate;

    @Column(nullable = true)
    private LocalDateTime lastUpdate;

    @ManyToOne
    private User author;

    @ManyToMany
    @JoinTable(
            name = "post_category",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private Set<Category> categories = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "post_tag",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<Tag> tags = new HashSet<>();

    private Integer commentCount;

    private Integer views;


    @Enumerated(EnumType.STRING)
    private PostStatus status;


    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Image> images = new ArrayList<>();


    public void addImage(Image image) {
        images.add(image);
        image.setPost(this);
    }

    public void removeImage(Image image) {
        images.remove(image);
        image.setPost(null);
    }

    // Additional fields like featured image, meta information, and status

    // Constructors, getters, setters, and other methods
}