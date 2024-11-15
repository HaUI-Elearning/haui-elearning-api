package com.elearning.haui.service;

import com.elearning.haui.domain.entity.Role;
import com.elearning.haui.repository.RoleRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService {

    private final RoleRepository roleRepository;

    @Autowired
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    // Phương thức tìm Role theo tên
    public Role findByName(String name) {
        return roleRepository.findByName(name);
    }

    // Nếu cần thiết, có thể thêm các phương thức khác như lưu Role, xóa Role, v.v.
}
