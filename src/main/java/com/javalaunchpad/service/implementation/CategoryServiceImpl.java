package com.javalaunchpad.service.implementation;

import com.javalaunchpad.entity.Category;
import com.javalaunchpad.exception.RessourceNotFoundException;
import com.javalaunchpad.repository.CategoryRepository;
import com.javalaunchpad.service.CategoryService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Category createCategory(Category category) {
        // Implement the logic to create a category
        return categoryRepository.save(category);
    }

    @Override
    public Category getCategoryById(Long categoryId) throws RessourceNotFoundException {
        // Implement the logic to retrieve a category by ID
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RessourceNotFoundException("Category not found"));
    }

    @Override
    public List<Category> getAllCategories() {
        // Implement the logic to retrieve all categories
        return categoryRepository.findAll();
    }

    @Override
    public void deleteCategory(Long categoryId) {
        // Implement the logic to delete a category
        categoryRepository.deleteById(categoryId);
    }

    // Other methods as per your requirements
}

