package com.javalaunchpad.javalaunchpad.post.repository;


import com.javalaunchpad.entity.Post;
import com.javalaunchpad.repository.PostRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@DataJpaTest
public class PostRepositoryTest {

    @Autowired
    private PostRepository underTest;


    private List<Post> posts = new ArrayList<>();

    @BeforeEach
    void setUp() {
        underTest.deleteAll();
        posts = Arrays.asList(
                Post.builder()
                        .excerpt("lean spring security architecture")
                        .content("learn spring sec architecture: authmanager, authprovider, userdetails, userdetailsservice")
                        .title("spring security")
                        .commentCount(100)
                        .views(10)
                        .build(),
                Post.builder()
                        .excerpt("java collections masterclass")
                        .content("learn manipulate sets lists maps")
                        .title("java collections")
                        .commentCount(50)
                        .views(20)
                        .build(),
                Post.builder()
                        .excerpt("React Hooks")
                        .content("learn use react hooks")
                        .title("React Hooks")
                        .commentCount(50)
                        .views(20)
                        .build());

        posts = underTest.saveAll(posts);
    }

    @Test
    public void itShouldReturnAllPosts() {
//        given
//        when
        List<Post> results = underTest.findAll();
//        then
        Assertions.assertThat(results).usingRecursiveComparison().ignoringCollectionOrder().isEqualTo(posts);
    }
}
