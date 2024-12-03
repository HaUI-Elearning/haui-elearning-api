package com.elearning.haui.api;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.elearning.haui.domain.entity.Category;
import com.elearning.haui.service.CategoryService;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/api/v1/categories")
public class CategoryAPI {
    private final CategoryService categoryService;

    public CategoryAPI(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("")
    public ResponseEntity<List<Category>> getHome() {

        return ResponseEntity.status(HttpStatus.OK).body(categoryService.getAllCategories());
    }

}
