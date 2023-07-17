package com.javalaunchpad.search.post;

import com.javalaunchpad.entity.Post;
import com.javalaunchpad.search.SearchCriteria;
import com.javalaunchpad.search.SearchOperation;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class PostSpecificationBuilder {
    private final List<SearchCriteria> params;

    public PostSpecificationBuilder() {
        this.params = new ArrayList<>();
    }

    public final PostSpecificationBuilder with(String key, String operation, Object value, List<String> values) {
        params.add(new SearchCriteria(key, operation, value, values));
        return this;
    }

    public final PostSpecificationBuilder with(SearchCriteria searchCriteria) {
        params.add(searchCriteria);
        return this;
    }

    public Specification<Post> build() {
        if (params.size() == 0) {
            return null;
        }

        Specification<Post> result = new PostSpecification(params.get(0));
        for (int idx = 1; idx < params.size(); idx++) {
            SearchCriteria criteria = params.get(idx);
            result = SearchOperation.getDataOption(criteria.getDataOption()) == SearchOperation.ALL
                    ? Specification.where(result).and(new PostSpecification(criteria))
                    : Specification.where(result).or(new PostSpecification(criteria));
        }

        return result;
    }
}
