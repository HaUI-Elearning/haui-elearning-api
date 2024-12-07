package com.elearning.haui.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.elearning.haui.domain.dto.CategoryDTO;
import com.elearning.haui.domain.entity.Category;
import com.elearning.haui.repository.CategoryRepository;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    // Phương thức ánh xạ từ một đối tượng Category sang CategoryDTO
    public CategoryDTO mapToCategoryDTO(Category category) {
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setCategoryId(category.getCategoryId());
        categoryDTO.setName(category.getName());
        categoryDTO.setDescription(category.getDescription());
        return categoryDTO;
    }

    // Phương thức ánh xạ từ danh sách Category sang danh sách CategoryDTO
    public List<CategoryDTO> mapToCategoryDTO(List<Category> categories) {
        return categories.stream()
                .map(this::mapToCategoryDTO)
                .collect(Collectors.toList());
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public CategoryDTO getCategoryById(Long id) {
        return mapToCategoryDTO(categoryRepository.findByCategoryId(id));
    }
}
