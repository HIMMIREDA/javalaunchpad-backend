package com.javalaunchpad.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class GetItemsResponse<T> {

    private int totalPages;
    private int currentPage;
    private Long totalItems;

    private List<T> items;
}
