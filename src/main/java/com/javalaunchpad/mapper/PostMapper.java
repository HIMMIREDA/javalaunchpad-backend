package com.javalaunchpad.mapper;

import com.javalaunchpad.dto.response.PostAdminResponse;
import com.javalaunchpad.dto.response.PostSuperAdminResponse;
import com.javalaunchpad.entity.Category;
import com.javalaunchpad.entity.Post;
import com.javalaunchpad.entity.Tag;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface PostMapper {
    @Mapping(source = "author.username", target = "author")
    @Mapping(source = "categories", target = "categories" , qualifiedByName = "mapCategoriesToString")
    @Mapping(source = "tags", target = "tags" , qualifiedByName = "mapTagsToString")
    PostSuperAdminResponse toPostSuperAdminResponse(Post post);

    @Named("mapCategoriesToString")
    default String mapCategoriesToString(Set<Category> categories) {
        // Implement your custom logic to map Set<Category> to String
        // For example, concatenate category names
        return categories.stream()
                .map(Category::getName)
                .collect(Collectors.joining(", "));
    }

    @Named("mapTagsToString")
    default String mapTagsToString(Set<Tag> tags) {
        // Implement your custom logic to map Set<Tag> to List<String>
        // For example, extract tag names into a list
        return tags.stream()
                .map(Tag::getName).collect(Collectors.joining(", "));
    }
}