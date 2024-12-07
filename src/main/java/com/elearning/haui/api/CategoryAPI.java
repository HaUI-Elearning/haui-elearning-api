package com.elearning.haui.api;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.elearning.haui.domain.dto.CategoryDTO;
import com.elearning.haui.service.CategoryService;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api/v1/categories")
public class CategoryAPI {
    private final CategoryService categoryService;

    public CategoryAPI(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("")
    public ResponseEntity<List<CategoryDTO>> getCategories() {

        return ResponseEntity.status(HttpStatus.OK)
                .body(categoryService.mapToCategoryDTO(categoryService.getAllCategories()));
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryDTO> getCategoryById(@PathVariable Long categoryId) {
        return ResponseEntity.ok(categoryService.getCategoryById(categoryId));
    }

}
