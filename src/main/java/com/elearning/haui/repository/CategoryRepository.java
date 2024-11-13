package com.elearning.haui.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.elearning.haui.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {

}
