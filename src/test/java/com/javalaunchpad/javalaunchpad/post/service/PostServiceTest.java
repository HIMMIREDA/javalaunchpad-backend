package com.javalaunchpad.javalaunchpad.post.service;

import com.javalaunchpad.dto.request.PostSearchRequest;
import com.javalaunchpad.entity.Category;
import com.javalaunchpad.entity.Post;
import com.javalaunchpad.entity.Tag;
import com.javalaunchpad.repository.PostRepository;
import com.javalaunchpad.search.SearchCriteria;
import com.javalaunchpad.security.Role;
import com.javalaunchpad.security.User;
import com.javalaunchpad.service.implementation.PostServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@ExtendWith(MockitoExtension.class)
public class PostServiceTest {

    @Mock
    private PostRepository postRepository;

    @InjectMocks
    private PostServiceImpl underTest;

    private static List<Post> posts = new ArrayList<>();

    @BeforeAll
    static void setUp() {
        Role adminRole = new Role(1L, "ADMIN");
        Role superAdminRole = new Role(2L, "SUPER_ADMIN");
        Set<Role> roleSet = new HashSet<>();
        roleSet.add(adminRole);
        roleSet.add(superAdminRole);
        Role userRole = new Role(null, "USER");
        Set<Role> roleSet1 = new HashSet<>();
        roleSet1.add(userRole);
        User user1 = new User(1L, "hamza", "nassour", "hamza.nassour13@gmail.com", "Hnas2018", roleSet, true);
        User user2 = new User(2L, "hamza", "nassour", "hamza.nassour@gmail.com", "Hnas2018", roleSet1, true);
        Category java = new Category(1L, "JAVA");
        Category spring = new Category(2L, "Spring");
        Category springSecurity = new Category(3L, "Spring Security");
        Category javascript = new Category(4L, "Javascript");
        Category react = new Category(5L, "React");
        Category frontend = new Category(6L, "Frontend");
        Category backend = new Category(7L, "Backend");
        Tag authorizationTag = new Tag(1L, "Authorization");
        Tag authenticationTag = new Tag(2L, "Authentication");
        Tag javaTag = new Tag(3L, "Java");
        Tag javascriptTag = new Tag(4L, "Javascript");
        Tag reactTag = new Tag(5L, "React");
        Tag collectionTag = new Tag(6L, "collections");

        posts = Arrays.asList(
                Post.builder()
                        .id(1L)
                        .author(user1)
                        .excerpt("learn spring security architecture")
                        .content("learn spring sec architecture: authmanager, authprovider, userdetails, userdetailsservice")
                        .title("spring security")
                        .publicationDate(LocalDateTime.of(2014, Month.JANUARY, 1, 10, 10, 30))
                        .tags(Stream.of(authorizationTag, authenticationTag, javaTag).collect(Collectors.toSet()))
                        .categories(Stream.of(java, spring, backend, springSecurity).collect(Collectors.toSet()))
                        .commentCount(100)
                        .views(500)
                        .build(),
                Post.builder()
                        .id(2L)
                        .author(user2)
                        .excerpt("java collections masterclass")
                        .publicationDate(LocalDateTime.now())
                        .content("learn manipulate sets lists maps")
                        .title("java collections")
                        .categories(Stream.of(java).collect(Collectors.toSet()))
                        .tags(Stream.of(javaTag, collectionTag).collect(Collectors.toSet()))
                        .commentCount(50)
                        .views(100)
                        .build(),
                Post.builder()
                        .id(3L)
                        .author(user2)
                        .excerpt("React Hooks")
                        .content("learn use react hooks")
                        .publicationDate(LocalDateTime.of(2013, Month.JANUARY, 1, 10, 10, 30))
                        .title("React Hooks")
                        .categories(Stream.of(javascript, react, frontend).collect(Collectors.toSet()))
                        .tags(Stream.of(javascriptTag, reactTag).collect(Collectors.toSet()))
                        .commentCount(50)
                        .views(20)
                        .build()
        );
    }


    @Test
    public void itShouldReturnAllPosts() {
//        given
        int page = 1;
        int count = posts.size();

        Pageable paging = PageRequest.of(page - 1, count);
        int start = (int) paging.getOffset();
        int end = Math.min((start + paging.getPageSize()), posts.size());


        Page<Post> expectedPostPage = new PageImpl<>(posts.subList(start, end), paging, posts.size());
        Mockito.when(postRepository.findAll(ArgumentMatchers.any(Pageable.class))).thenReturn(expectedPostPage);
//        when
        Page<Post> postPage = underTest.searchPosts(page, count, null, null, null);

//        then
        ArgumentCaptor<Pageable> pageableArgumentCaptor = ArgumentCaptor.forClass(Pageable.class);

        Mockito.verify(postRepository).findAll(pageableArgumentCaptor.capture());

        Assertions.assertThat(pageableArgumentCaptor.getValue().getPageSize()).isEqualTo(count);
        Assertions.assertThat(pageableArgumentCaptor.getValue().getPageNumber()).isEqualTo(page);
        Assertions.assertThat(postPage.getTotalElements()).isEqualTo(posts.size());

        Assertions.assertThat(postPage).usingRecursiveComparison().ignoringCollectionOrder().isEqualTo(expectedPostPage);

    }

    @Test
    public void itShouldReturnPostsHavingACategoryContainingSpring() {
//        given
        int page = 1;
        int count = posts.size();
        String filterValue = "Spring";
        PostSearchRequest postSearchRequest = PostSearchRequest.builder()
                .dataOption("all")
                .searchCriteriaList(
                        Arrays.asList(
                                SearchCriteria.builder()
                                        .filterKey("category.name")
                                        .operation("cn")
                                        .value(filterValue)
                                        .build()
                        )
                )
                .build();

        List<Post> expectedPosts = posts.stream().filter(post -> post.getCategories().stream().anyMatch(category -> category.getName().toLowerCase().contains(filterValue.toLowerCase()))).toList();
        Pageable paging = PageRequest.of(page - 1, count);
        int start = (int) paging.getOffset();
        int end = Math.min((start + paging.getPageSize()), expectedPosts.size());


        Page<Post> expectedPostPage = new PageImpl<>(expectedPosts.subList(start, end), paging, expectedPosts.size());
        Mockito.when(postRepository.findAll(ArgumentMatchers.<Specification<Post>>any(), ArgumentMatchers.any(Pageable.class))).thenReturn(expectedPostPage);
//        when
        Page<Post> postPage = underTest.searchPosts(page, count, postSearchRequest, null, null);

//        then
        Mockito.verify(postRepository).findAll(ArgumentMatchers.<Specification<Post>>any(), ArgumentMatchers.any(Pageable.class));
        Assertions.assertThat(postPage).usingRecursiveComparison().ignoringCollectionOrder().isEqualTo(expectedPostPage);

    }


    @Test
    void itShouldReturnPostsNotHavingACategoryContainingSpring() {
//        given
        int page = 1;
        int count = posts.size();
        String filterValue = "Spring";

        PostSearchRequest postSearchRequest = PostSearchRequest.builder()
                .dataOption("all")
                .searchCriteriaList(
                        Arrays.asList(
                                SearchCriteria.builder()
                                        .filterKey("category.name")
                                        .operation("nc")
                                        .value(filterValue)
                                        .build()
                        )
                )
                .build();


        List<Post> expectedPosts = posts.stream().filter(post -> post.getCategories().stream().noneMatch(category -> category.getName().toLowerCase().contains(filterValue.toLowerCase()))).toList();
        Pageable paging = PageRequest.of(page - 1, count);
        int start = (int) paging.getOffset();
        int end = Math.min((start + paging.getPageSize()), expectedPosts.size());


        Page<Post> expectedPostPage = new PageImpl<>(expectedPosts.subList(start, end), paging, expectedPosts.size());
        Mockito.when(postRepository.findAll(ArgumentMatchers.<Specification<Post>>any(), ArgumentMatchers.any(Pageable.class))).thenReturn(expectedPostPage);
//        when
        Page<Post> postPage = underTest.searchPosts(page, count, postSearchRequest, null, null);

//        then
        Mockito.verify(postRepository).findAll(ArgumentMatchers.<Specification<Post>>any(), ArgumentMatchers.any(Pageable.class));
        Assertions.assertThat(postPage).usingRecursiveComparison().ignoringCollectionOrder().isEqualTo(expectedPostPage);
    }

    @Test
    void itShouldReturnPostsHavingATitleEndingWithSecurityOrHavingACategoryEqualToReact() {
        //        given
        int page = 1;
        int count = posts.size();
        String titleFilterValue = "Security";
        String categoryFilterValue = "React";

        PostSearchRequest postSearchRequest = PostSearchRequest.builder()
                .dataOption("any")
                .searchCriteriaList(
                        Arrays.asList(
                                SearchCriteria.builder()
                                        .filterKey("title")
                                        .operation("ew")
                                        .value(titleFilterValue)
                                        .build(),
                                SearchCriteria.builder()
                                        .filterKey("category.name")
                                        .operation("eq")
                                        .value(categoryFilterValue)
                                        .build()
                        )
                )
                .build();


        List<Post> expectedPosts = posts.stream().filter(
                post ->
                        post.getCategories().stream().anyMatch(category -> category.getName().equalsIgnoreCase(categoryFilterValue)) || post.getTitle().toLowerCase().endsWith(titleFilterValue.toLowerCase())
        ).toList();

        Pageable paging = PageRequest.of(page - 1, count);
        int start = (int) paging.getOffset();
        int end = Math.min((start + paging.getPageSize()), expectedPosts.size());


        Page<Post> expectedPostPage = new PageImpl<>(expectedPosts.subList(start, end), paging, expectedPosts.size());
        Mockito.when(postRepository.findAll(ArgumentMatchers.<Specification<Post>>any(), ArgumentMatchers.any(Pageable.class))).thenReturn(expectedPostPage);
//        when
        Page<Post> postPage = underTest.searchPosts(page, count, postSearchRequest, null, null);

//        then
        Mockito.verify(postRepository).findAll(ArgumentMatchers.<Specification<Post>>any(), ArgumentMatchers.any(Pageable.class));
        System.out.println(expectedPostPage.getContent());
        Assertions.assertThat(postPage).usingRecursiveComparison().ignoringCollectionOrder().isEqualTo(expectedPostPage.getContent());
    }

    @Test
    void itShouldReturnAllPostsPaginated() {
//        given
        int page = 1;
        int count = 1;

        Pageable paging = PageRequest.of(page - 1, count);
        int start = (int) paging.getOffset();
        int end = Math.min((start + paging.getPageSize()), posts.size());
        Page<Post> expectedPostPage = new PageImpl<>(posts.subList(start, end), paging, posts.size());


        Mockito.when(postRepository.findAll(ArgumentMatchers.any(Pageable.class))).thenReturn(expectedPostPage);
//        when
        Page<Post> postPage = underTest.searchPosts(page, count, null, null, null);

//        then
        Mockito.verify(postRepository).findAll(ArgumentMatchers.any(Pageable.class));
        Assertions.assertThat(postPage.getTotalPages()).isEqualTo(expectedPostPage.getTotalPages());
        Assertions.assertThat(postPage.getTotalElements()).isEqualTo(expectedPostPage.getTotalElements());
        Assertions.assertThat(postPage.getContent().size()).isEqualTo(count);
        Assertions.assertThat(postPage.getNumber() + 1).isEqualTo(page);
    }

    @Test
    void itShouldReturnPostsContainingATagWithNameCollectionsOrAuthorizationPaginatedAndSortedByViews() {
//        given
        int page = 1;
        int count = posts.size();

        List<String> tagFilterValues = Arrays.asList(
                "Authorization",
                "Collections"
        );


        PostSearchRequest postSearchRequest = PostSearchRequest.builder()
                .dataOption("all")
                .searchCriteriaList(
                        Collections.singletonList(
                                SearchCriteria.builder()
                                        .filterKey("tag.name")
                                        .operation("in")
                                        .values(tagFilterValues)
                                        .build()
                        )
                )
                .build();


        List<Post> expectedPosts = posts.stream().filter(
                post ->
                        post.getTags().stream().anyMatch(tag -> tagFilterValues.stream().map(String::toLowerCase).toList().contains(tag.getName().toLowerCase()))
        ).toList();

        Pageable paging = PageRequest.of(page - 1, count);
        int start = (int) paging.getOffset();
        int end = Math.min((start + paging.getPageSize()), expectedPosts.size());
        Page<Post> expectedPostPage = new PageImpl<>(expectedPosts.subList(start, end), paging, expectedPosts.size());


        Mockito.when(postRepository.findAll(ArgumentMatchers.<Specification<Post>>any(), ArgumentMatchers.any(Pageable.class))).thenReturn(expectedPostPage);
//        when
        Page<Post> postPage = underTest.searchPosts(page, count, postSearchRequest, null, null);

//        then
        Mockito.verify(postRepository).findAll(ArgumentMatchers.<Specification<Post>>any(), ArgumentMatchers.any(Pageable.class));
        Assertions.assertThat(postPage).usingRecursiveComparison().isEqualTo(expectedPostPage);
    }


    @Test
    void itShouldReturnAllPostsSortedByViewsAsc() {
//        given
        int page = 1;
        int count = posts.size();
        String sortBy = "views";
        String sortOrder = "ASC";



        List<Post> expectedPosts = posts.stream().sorted((Comparator.comparing(Post::getViews))).toList();

        Pageable paging = PageRequest.of(page - 1, count);
        int start = (int) paging.getOffset();
        int end = Math.min((start + paging.getPageSize()), expectedPosts.size());
        Page<Post> expectedPostPage = new PageImpl<>(expectedPosts.subList(start, end), paging, expectedPosts.size());


        Mockito.when(postRepository.findAll(ArgumentMatchers.any(Pageable.class))).thenReturn(expectedPostPage);
//        when
        Page<Post> postPage = underTest.searchPosts(page, count, null, sortBy, sortOrder);

//        then
        Mockito.verify(postRepository).findAll(ArgumentMatchers.any(Pageable.class));
        Assertions.assertThat(postPage).usingRecursiveComparison().ignoringCollectionOrder().isEqualTo(expectedPostPage);
    }

    @Test
    void itShouldReturnPostsNotHavingACategoryOfJavaOrSpringSortedByPublicationDateDesc() {
//        given
        int page = 1;
        int count = posts.size();
        String sortBy = "publicationDate";
        String sortOrder = "DESC";
        List<String> filterValues = Arrays.asList("Spring", "Java");


        PostSearchRequest postSearchRequest = PostSearchRequest.builder()
                .searchCriteriaList(
                        Arrays.asList(
                                SearchCriteria.builder()
                                        .filterKey("category.name")
                                        .operation("nin")
                                        .values(filterValues)
                                        .build()
                        )
                ).build();


        List<Post> expectedPosts = posts.stream().sorted((Comparator.comparing(Post::getPublicationDate).reversed())).toList();

        Pageable paging = PageRequest.of(page - 1, count);
        int start = (int) paging.getOffset();
        int end = Math.min((start + paging.getPageSize()), expectedPosts.size());
        Page<Post> expectedPostPage = new PageImpl<>(expectedPosts.subList(start, end), paging, expectedPosts.size());


        Mockito.when(postRepository.findAll(ArgumentMatchers.<Specification<Post>>any(), ArgumentMatchers.any(Pageable.class))).thenReturn(expectedPostPage);
//        when
        Page<Post> postPage = underTest.searchPosts(page, count, postSearchRequest, sortBy, sortOrder);

//        then
        Mockito.verify(postRepository).findAll(ArgumentMatchers.<Specification<Post>>any(), ArgumentMatchers.any(Pageable.class));
        Assertions.assertThat(postPage).usingRecursiveComparison().ignoringCollectionOrder().isEqualTo(expectedPostPage);
    }
}
