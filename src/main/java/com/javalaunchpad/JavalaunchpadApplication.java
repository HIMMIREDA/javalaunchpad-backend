package com.javalaunchpad;

import com.javalaunchpad.entity.Category;
import com.javalaunchpad.entity.Post;
import com.javalaunchpad.entity.Tag;
import com.javalaunchpad.repository.CategoryRepository;
import com.javalaunchpad.repository.PostRepository;
import com.javalaunchpad.repository.TagRepository;
import com.javalaunchpad.security.Role;
import com.javalaunchpad.security.RoleRepository;
import com.javalaunchpad.security.User;
import com.javalaunchpad.security.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SpringBootApplication
public class JavalaunchpadApplication implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private PostRepository postRepository;

    public static void main(String[] args) {
        SpringApplication.run(JavalaunchpadApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        Role adminRole = new Role(null, "ADMIN");
        Role superAdminRole = new Role(null, "SUPER_ADMIN");
        Set<Role> roleSet = new HashSet<>();
        roleSet.add(adminRole);
        roleSet.add(superAdminRole);
        Role userRole = new Role(null, "USER");
        Set<Role> roleSet1 = new HashSet<>();
        roleSet1.add(userRole);
        roleRepository.save(adminRole);
        roleRepository.save(superAdminRole);
        roleRepository.save(userRole);
        User user1 = new User(null, "hamza", "nassour", "hamza.nassour13@gmail.com", passwordEncoder.encode("Hnas2018"), roleSet, true);
        User user2 = new User(null, "hamza", "nassour", "hamza.nassour@gmail.com", passwordEncoder.encode("Hnas2018"), roleSet1, true);
        userRepository.save(user1);
        userRepository.save(user2);
        Category java = new Category(null, "JAVA");
        Category spring = new Category(null, "Spring");
        Category springSecurity = new Category(null, "Spring Security");
        Category javascript = new Category(null, "Javascript");
        Category react = new Category(null, "React");
        Category frontend = new Category(null, "Frontend");
        Category backend = new Category(null, "Backend");
        categoryRepository.save(java);
        categoryRepository.save(spring);
        categoryRepository.save(springSecurity);
        categoryRepository.save(javascript);
        categoryRepository.save(react);
        categoryRepository.save(frontend);
        categoryRepository.save(backend);
        Tag authorizationTag = new Tag(null, "Authorization");
        Tag authenticationTag = new Tag(null, "Authentication");
        Tag javaTag = new Tag(null, "Java");
        Tag javascriptTag = new Tag(null, "Javascript");
        Tag reactTag = new Tag(null, "React");
        Tag collectionTag = new Tag(null, "collections");
        tagRepository.save(authorizationTag);
        tagRepository.save(authenticationTag);
        tagRepository.save(javaTag);
        tagRepository.save(collectionTag);
        tagRepository.save(javascriptTag);
        tagRepository.save(reactTag);

//		posts
        List<Post> posts = Arrays.asList(
                Post.builder()
                        .author(user1)
                        .excerpt("lean spring security architecture")
                        .content("learn spring sec architecture: authmanager, authprovider, userdetails, userdetailsservice")
                        .title("spring security")
                        .tags(Stream.of(authorizationTag, authenticationTag).collect(Collectors.toSet()))
                        .categories(Stream.of(java, spring, backend, springSecurity).collect(Collectors.toSet()))
                        .commentCount(100)
                        .views(10)
                        .build(),
                Post.builder()
                        .author(user2)
                        .excerpt("java collections masterclass")
                        .content("learn manipulate sets lists maps")
                        .title("java collections")
                        .categories(Stream.of(java).collect(Collectors.toSet()))
                        .tags(Stream.of(javaTag, collectionTag).collect(Collectors.toSet()))
                        .commentCount(50)
                        .views(20)
                        .build(),
                Post.builder()
                        .author(user2)
                        .excerpt("React Hooks")
                        .content("learn use react hooks")
                        .title("React Hooks")
                        .categories(Stream.of(javascript,react, frontend).collect(Collectors.toSet()))
                        .tags(Stream.of(javascriptTag, reactTag).collect(Collectors.toSet()))
                        .commentCount(50)
                        .views(20)
                        .build()
        );

        postRepository.saveAll(posts);
    }
}