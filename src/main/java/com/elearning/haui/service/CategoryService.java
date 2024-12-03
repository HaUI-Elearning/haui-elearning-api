package com.elearning.haui.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.elearning.haui.domain.entity.Category;
import com.elearning.haui.repository.CategoryRepository;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }
}
