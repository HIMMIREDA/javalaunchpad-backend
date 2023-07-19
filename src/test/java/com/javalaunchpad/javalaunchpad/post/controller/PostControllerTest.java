package com.javalaunchpad.javalaunchpad.post.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.javalaunchpad.dto.request.PostSearchRequest;
import com.javalaunchpad.dto.response.GetItemsResponse;
import com.javalaunchpad.entity.Category;
import com.javalaunchpad.entity.Post;
import com.javalaunchpad.entity.Tag;
import com.javalaunchpad.repository.PostRepository;
import com.javalaunchpad.search.SearchCriteria;
import com.javalaunchpad.security.Role;
import com.javalaunchpad.security.User;
import com.javalaunchpad.service.ImageStorageService;
import com.javalaunchpad.service.TagService;
import com.javalaunchpad.service.implementation.ImageStorageServiceImpl;
import com.javalaunchpad.service.implementation.PostServiceImpl;
import com.javalaunchpad.service.implementation.TagServiceImpl;
import com.javalaunchpad.web.PostController;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@WebMvcTest(controllers = PostController.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc(addFilters = false)
public class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PostServiceImpl postService;

    @MockBean
    private ImageStorageServiceImpl imageStorageService;

    @MockBean
    private TagServiceImpl tagService;

    @MockBean
    private PostRepository postRepository;

    @Autowired
    private ObjectMapper objectMapper;

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
    public void itShouldReturn200AndPostsHavingPublicationDateBefore2014SortedByViewsASCPaginated() throws Exception {
//        given
        String filterValue = "2014-01-01 11:11:11";
        LocalDateTime parsedFilterValue = LocalDateTime.parse(filterValue, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        int page = 1;
        int count = 1;
        String sortBy = "views";
        String sortOrder = "ASC";

        PostSearchRequest postSearchRequest = PostSearchRequest.builder()
                .searchCriteriaList(
                        Collections.singletonList(
                                SearchCriteria.builder()
                                        .filterKey("publicationDate")
                                        .operation("le")
                                        .value(filterValue)
                                        .build()
                        )
                )
                .build();

        List<Post> expectedPosts = posts.stream().filter(post -> post.getPublicationDate().isBefore(parsedFilterValue)).sorted(Comparator.comparing(Post::getViews)).toList();
        Pageable paging = PageRequest.of(page - 1, count);
        int start = (int) paging.getOffset();
        int end = Math.min((start + paging.getPageSize()), expectedPosts.size());


        Page<Post> expectedPostPage = new PageImpl<>(expectedPosts.subList(start, end), paging, expectedPosts.size());
        Mockito.when(postService.searchPosts(ArgumentMatchers.anyInt(), ArgumentMatchers.anyInt(), ArgumentMatchers.any(PostSearchRequest.class), ArgumentMatchers.anyString(), ArgumentMatchers.anyString())).thenReturn(expectedPostPage);

//        when
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("sortBy", sortBy);
        queryParams.add("sortOrder", sortOrder);
        queryParams.add("page", String.valueOf(page));
        queryParams.add("count", String.valueOf(count));

        MvcResult mvcResult = mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/posts/search")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(postSearchRequest))
                                .queryParams(queryParams)
                ).andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String responseBody = mvcResult.getResponse().getContentAsString();
        GetItemsResponse<Post> responseObject = objectMapper.readValue(responseBody, new TypeReference<>() {
        });


//        then
        ArgumentCaptor<PostSearchRequest> postSearchRequestArgumentCaptor = ArgumentCaptor.forClass(PostSearchRequest.class);
        ArgumentCaptor<Integer> pageArgumentCaptor = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<Integer> countArgumentCaptor = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<String> sortByArgumentCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> sortOrderArgumentCaptor = ArgumentCaptor.forClass(String.class);

        Mockito.verify(postService).searchPosts(pageArgumentCaptor.capture(),countArgumentCaptor.capture(),postSearchRequestArgumentCaptor.capture(),sortByArgumentCaptor.capture(),sortOrderArgumentCaptor.capture());


        Assertions.assertThat(pageArgumentCaptor.getValue()).isEqualTo(page - 1);
        Assertions.assertThat(countArgumentCaptor.getValue()).isEqualTo(count);
        Assertions.assertThat(sortByArgumentCaptor.getValue()).isEqualTo(sortBy);
        Assertions.assertThat(sortOrderArgumentCaptor.getValue()).isEqualTo(sortOrder);
        Assertions.assertThat(postSearchRequestArgumentCaptor.getValue()).usingRecursiveComparison().ignoringCollectionOrder().isEqualTo(postSearchRequest);
        Assertions.assertThat(responseObject.getItems()).usingRecursiveComparison().ignoringCollectionOrder().isEqualTo(expectedPostPage.getContent());
        Assertions.assertThat(responseObject.getTotalItems()).isEqualTo(expectedPostPage.getTotalElements());
        Assertions.assertThat(responseObject.getCurrentPage()).isEqualTo(expectedPostPage.getNumber() + 1);
        Assertions.assertThat(responseObject.getTotalPages()).isEqualTo(expectedPostPage.getTotalPages());
    }
}
