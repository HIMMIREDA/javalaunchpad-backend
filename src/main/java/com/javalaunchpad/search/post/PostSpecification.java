package com.javalaunchpad.search.post;

import com.javalaunchpad.entity.Category;
import com.javalaunchpad.entity.Post;
import com.javalaunchpad.entity.Tag;
import com.javalaunchpad.search.SearchCriteria;
import com.javalaunchpad.search.SearchOperation;
import com.javalaunchpad.security.User;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class PostSpecification implements Specification<Post> {
    private final SearchCriteria searchCriteria;

    public PostSpecification(final SearchCriteria searchCriteria) {
        super();
        this.searchCriteria = searchCriteria;
    }

    @Override
    public Predicate toPredicate(Root<Post> root, CriteriaQuery<?> query, CriteriaBuilder cb) {

        String strToSearch = Objects.requireNonNullElse(searchCriteria.getValue(), "").toString().toLowerCase();
        List<String> listOfStrToSearch = searchCriteria.getValues().stream().map(String::toLowerCase).toList();

        switch (Objects.requireNonNull(SearchOperation.getSimpleOperation(searchCriteria.getOperation()))) {
            case CONTAINS -> {
                if (searchCriteria.getFilterKey().equalsIgnoreCase("category.name")) {
                    Subquery<Long> subquery = query.subquery(Long.class);
                    Root<Category> category = subquery.from(Category.class);
                    subquery.select(category.join("posts").get("id"))
                            .where(cb.like(cb.lower(category.get("name")), "%" + strToSearch + "%"));
                    return root.get("id").in(subquery);
                }
                if (searchCriteria.getFilterKey().equalsIgnoreCase("tag.name")) {
                    Subquery<Long> subquery = query.subquery(Long.class);
                    Root<Tag> tag = subquery.from(Tag.class);
                    subquery.select(tag.join("posts").get("id"))
                            .where(cb.like(cb.lower(tag.get("name")), "%" + strToSearch + "%"));
                    return root.get("id").in(subquery);
                }
                if (searchCriteria.getFilterKey().equalsIgnoreCase("author.username")) {
                    return cb.like(cb.lower(authorJoin(root).<String>get("username")), "%" + strToSearch + "%");
                }
                return cb.like(cb.lower(root.get(searchCriteria.getFilterKey())), "%" + strToSearch + "%");
            }
            case DOES_NOT_CONTAIN -> {
                if (searchCriteria.getFilterKey().equalsIgnoreCase("category.name")) {
                    Subquery<Long> subquery = query.subquery(Long.class);
                    Root<Category> category = subquery.from(Category.class);
                    subquery.select(category.join("posts").get("id"))
                            .where(cb.like(cb.lower(category.get("name")), "%" + strToSearch + "%"));
                    return cb.not(root.get("id").in(subquery));
                }
                if (searchCriteria.getFilterKey().equalsIgnoreCase("tag.name")) {
                    Subquery<Long> subquery = query.subquery(Long.class);
                    Root<Tag> tag = subquery.from(Tag.class);
                    subquery.select(tag.join("posts").get("id"))
                            .where(cb.like(cb.lower(tag.get("name")), "%" + strToSearch + "%"));
                    return cb.not(root.get("id").in(subquery));
                }
                if (searchCriteria.getFilterKey().equalsIgnoreCase("author.username")) {
                    return cb.notLike(cb.lower(authorJoin(root).<String>get("username")), "%" + strToSearch + "%");
                }
                return cb.notLike(cb.lower(root.get(searchCriteria.getFilterKey())), "%" + strToSearch + "%");
            }
            case BEGINS_WITH -> {
                if (searchCriteria.getFilterKey().equalsIgnoreCase("category.name")) {
                    Subquery<Long> subquery = query.subquery(Long.class);
                    Root<Category> category = subquery.from(Category.class);
                    subquery.select(category.join("posts").get("id"))
                            .where(cb.like(cb.lower(category.get("name")), strToSearch + "%"));
                    return root.get("id").in(subquery);
                }
                if (searchCriteria.getFilterKey().equalsIgnoreCase("tag.name")) {
                    Subquery<Long> subquery = query.subquery(Long.class);
                    Root<Tag> tag = subquery.from(Tag.class);
                    subquery.select(tag.join("posts").get("id"))
                            .where(cb.like(cb.lower(tag.get("name")), strToSearch + "%"));
                    return root.get("id").in(subquery);
                }
                if (searchCriteria.getFilterKey().equalsIgnoreCase("author.username")) {
                    return cb.like(cb.lower(authorJoin(root).<String>get("username")), strToSearch + "%");
                }
                return cb.like(cb.lower(root.get(searchCriteria.getFilterKey())), strToSearch + "%");
            }
            case DOES_NOT_BEGIN_WITH -> {
                if (searchCriteria.getFilterKey().equalsIgnoreCase("category.name")) {
                    Subquery<Long> subquery = query.subquery(Long.class);
                    Root<Category> category = subquery.from(Category.class);
                    subquery.select(category.join("posts").get("id"))
                            .where(cb.like(cb.lower(category.get("name")), strToSearch + "%"));
                    return cb.not(root.get("id").in(subquery));
                }
                if (searchCriteria.getFilterKey().equalsIgnoreCase("tag.name")) {
                    Subquery<Long> subquery = query.subquery(Long.class);
                    Root<Tag> tag = subquery.from(Tag.class);
                    subquery.select(tag.join("posts").get("id"))
                            .where(cb.like(cb.lower(tag.get("name")), strToSearch + "%"));
                    return cb.not(root.get("id").in(subquery));
                }
                if (searchCriteria.getFilterKey().equalsIgnoreCase("author.username")) {
                    return cb.notLike(cb.lower(authorJoin(root).<String>get("username")), strToSearch + "%");
                }
                return cb.notLike(cb.lower(root.get(searchCriteria.getFilterKey())), strToSearch + "%");
            }
            case ENDS_WITH -> {
                if (searchCriteria.getFilterKey().equalsIgnoreCase("category.name")) {
                    Subquery<Long> subquery = query.subquery(Long.class);
                    Root<Category> category = subquery.from(Category.class);
                    subquery.select(category.join("posts").get("id"))
                            .where(cb.like(cb.lower(category.get("name")), "%" + strToSearch));
                    return root.get("id").in(subquery);
                }
                if (searchCriteria.getFilterKey().equalsIgnoreCase("tag.name")) {
                    Subquery<Long> subquery = query.subquery(Long.class);
                    Root<Tag> tag = subquery.from(Tag.class);
                    subquery.select(tag.join("posts").get("id"))
                            .where(cb.like(cb.lower(tag.get("name")), "%" + strToSearch));
                    return root.get("id").in(subquery);
                }
                if (searchCriteria.getFilterKey().equalsIgnoreCase("author.username")) {
                    return cb.like(cb.lower(authorJoin(root).<String>get("username")), "%" + strToSearch);
                }
                return cb.like(cb.lower(root.get(searchCriteria.getFilterKey())), "%" + strToSearch);
            }
            case DOES_NOT_END_WITH -> {
                if (searchCriteria.getFilterKey().equalsIgnoreCase("category.name")) {
                    Subquery<Long> subquery = query.subquery(Long.class);
                    Root<Category> category = subquery.from(Category.class);
                    subquery.select(category.join("posts").get("id"))
                            .where(cb.like(cb.lower(category.get("name")), "%" + strToSearch));
                    return cb.not(root.get("id").in(subquery));
                }
                if (searchCriteria.getFilterKey().equalsIgnoreCase("tag.name")) {
                    Subquery<Long> subquery = query.subquery(Long.class);
                    Root<Tag> tag = subquery.from(Tag.class);
                    subquery.select(tag.join("posts").get("id"))
                            .where(cb.like(cb.lower(tag.get("name")), "%" + strToSearch));
                    return cb.not(root.get("id").in(subquery));
                }
                if (searchCriteria.getFilterKey().equalsIgnoreCase("author.username")) {
                    return cb.notLike(cb.lower(authorJoin(root).<String>get("username")), "%" + strToSearch);
                }
                return cb.notLike(cb.lower(root.get(searchCriteria.getFilterKey())), "%" + strToSearch);
            }
            case EQUAL -> {
                if (searchCriteria.getFilterKey().equalsIgnoreCase("category.name")) {
                    Subquery<Long> subquery = query.subquery(Long.class);
                    Root<Category> category = subquery.from(Category.class);
                    subquery.select(category.join("posts").get("id"))
                            .where(cb.equal(cb.lower(category.get("name")), strToSearch));
                    return root.get("id").in(subquery);
                }
                if (searchCriteria.getFilterKey().equalsIgnoreCase("tag.name")) {
                    Subquery<Long> subquery = query.subquery(Long.class);
                    Root<Tag> tag = subquery.from(Tag.class);
                    subquery.select(tag.join("posts").get("id"))
                            .where(cb.equal(cb.lower(tag.get("name")), strToSearch));
                    return root.get("id").in(subquery);
                }
                if (searchCriteria.getFilterKey().equalsIgnoreCase("author.username")) {
                    return cb.equal(cb.lower(authorJoin(root).<String>get("username")), strToSearch);
                }
                return cb.equal(cb.lower(root.get(searchCriteria.getFilterKey())), strToSearch);
            }
            case NOT_EQUAL -> {
                if (searchCriteria.getFilterKey().equalsIgnoreCase("category.name")) {
                    Subquery<Long> subquery = query.subquery(Long.class);
                    Root<Category> category = subquery.from(Category.class);
                    subquery.select(category.join("posts").get("id"))
                            .where(cb.equal(cb.lower(category.get("name")), strToSearch));
                    return cb.not(root.get("id").in(subquery));
                }
                if (searchCriteria.getFilterKey().equalsIgnoreCase("tag.name")) {
                    Subquery<Long> subquery = query.subquery(Long.class);
                    Root<Tag> tag = subquery.from(Tag.class);
                    subquery.select(tag.join("posts").get("id"))
                            .where(cb.equal(cb.lower(tag.get("name")), strToSearch));
                    return cb.not(root.get("id").in(subquery));
                }
                if (searchCriteria.getFilterKey().equalsIgnoreCase("author.username")) {
                    return cb.notEqual(cb.lower(authorJoin(root).<String>get("username")), strToSearch);
                }
                return cb.notEqual(cb.lower(root.get(searchCriteria.getFilterKey())), strToSearch);
            }
            case NUL -> {
                return cb.isNull(root.get(searchCriteria.getFilterKey()));
            }
            case NOT_NULL -> {
                return cb.isNotNull(root.get(searchCriteria.getFilterKey()));
            }
            case GREATER_THAN -> {
                return cb.greaterThan(root.<String>get(searchCriteria.getFilterKey()), searchCriteria.getValue().toString());
            }
            case GREATER_THAN_EQUAL -> {
                return cb.greaterThanOrEqualTo(root.<String>get(searchCriteria.getFilterKey()), searchCriteria.getValue().toString());
            }
            case LESS_THAN -> {
                return cb.lessThan(root.<String>get(searchCriteria.getFilterKey()), searchCriteria.getValue().toString());
            }
            case LESS_THAN_EQUAL -> {
                return cb.lessThanOrEqualTo(root.<String>get(searchCriteria.getFilterKey()), searchCriteria.getValue().toString());
            }
            case IN -> {
                if (searchCriteria.getFilterKey().equalsIgnoreCase("category.name")) {
                    Subquery<Long> subquery = query.subquery(Long.class);
                    Root<Category> category = subquery.from(Category.class);
                    subquery.select(category.join("posts").get("id"))
                            .where(cb.lower(category.get("name")).in(listOfStrToSearch));
                    return root.get("id").in(subquery);
                }
                if (searchCriteria.getFilterKey().equalsIgnoreCase("tag.name")) {
                    Subquery<Long> subquery = query.subquery(Long.class);
                    Root<Tag> tag = subquery.from(Tag.class);
                    subquery.select(tag.join("posts").get("id"))
                            .where(cb.lower(tag.get("name")).in(listOfStrToSearch));
                    return root.get("id").in(subquery);
                }
                return cb.lower(root.<String>get(searchCriteria.getFilterKey())).in(listOfStrToSearch);
            }
            case NIN -> {
                if (searchCriteria.getFilterKey().equalsIgnoreCase("category.name")) {
                    Subquery<Long> subquery = query.subquery(Long.class);
                    Root<Category> category = subquery.from(Category.class);
                    subquery.select(category.join("posts").get("id"))
                            .where(cb.lower(category.get("name")).in(listOfStrToSearch));
                    return cb.not(root.get("id").in(subquery));
                }
                if (searchCriteria.getFilterKey().equalsIgnoreCase("tag.name")) {
                    Subquery<Long> subquery = query.subquery(Long.class);
                    Root<Tag> tag = subquery.from(Tag.class);
                    subquery.select(tag.join("posts").get("id"))
                            .where(cb.lower(tag.get("name")).in(listOfStrToSearch));
                    return cb.not(root.get("id").in(subquery));
                }
                return cb.lower(root.<String>get(searchCriteria.getFilterKey())).in(listOfStrToSearch).not();
            }
        }
        return null;
    }

    private Join<Post, Category> categoryJoin(Root<Post> root) {
        return root.join("categories");
    }

    private Join<Post, Tag> tagJoin(Root<Post> root) {
        return root.join("tags");
    }

    private Join<Post, User> authorJoin(Root<Post> root) {
        return root.join("author");
    }
}
