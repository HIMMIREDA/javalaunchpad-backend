package com.javalaunchpad.service;

import com.javalaunchpad.entity.Category;
import com.javalaunchpad.exception.RessourceNotFoundException;

import java.util.List;

public interface CategoryService {
    Category createCategory(Category category);
    Category getCategoryById(Long categoryId) throws RessourceNotFoundException;
    List<Category> getAllCategories();
    void deleteCategory(Long categoryId);
    // Other methods as per your requirements
}

