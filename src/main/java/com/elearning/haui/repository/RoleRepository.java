package com.elearning.haui.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.elearning.haui.domain.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);
}
