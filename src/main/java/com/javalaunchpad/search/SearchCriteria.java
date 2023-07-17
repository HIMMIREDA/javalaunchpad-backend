package com.javalaunchpad.search;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchCriteria {

//    name of field that we will filter by ex: title, content, tag.name, category.name, author.name,..
    private String filterKey;
//    value of field
    private Object value;
//    operation: like, cn, eq, ne, in, ...
    private String operation;
//    dataOption: "all", "any"
//    defaultValue: "all"
//    (if "all" used the current predicate will be AND with the next predicate , if "any" it will be OR with next predicate)
    private String dataOption;
//    values will be used in case of "IN" operation
    private List<String> values = new ArrayList<>();


    public SearchCriteria(String filterKey, String operation, Object value,List<String> values){
        super();
        this.filterKey = filterKey;
        this.operation = operation;
        this.value = value;
        this.values = values;
    }
}
