package com.javalaunchpad.dto.request;

import com.javalaunchpad.search.SearchCriteria;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PostSearchRequest {
    private List<SearchCriteria> searchCriteriaList;
    private String dataOption = "all";
}
