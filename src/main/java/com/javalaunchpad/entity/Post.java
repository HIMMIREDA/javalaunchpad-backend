package com.javalaunchpad.entity;

import com.javalaunchpad.entity.enumeration.PostStatus;
import com.javalaunchpad.security.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
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
    private Set<Category> categories;

    @ManyToMany
    private Set<Tag> tags;

    private Integer commentCount;


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