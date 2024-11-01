package com.elearning.haui.controller.Admin;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import com.elearning.haui.entity.Role;
import com.elearning.haui.entity.User;

import com.elearning.haui.service.RoleService;
import com.elearning.haui.service.UserService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserController {
    private final UserService userService;
    private final RoleService roleService;

    public UserController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping("/")
    public String hello() {
        return "hello";
    }

    @GetMapping("/admin/user")
    public String getUserPage(Model model) {
        List<User> users = this.userService.getAllUsers();
        model.addAttribute("users", users);
        return "admin/user/show";
    }

    @GetMapping("/admin/user/create")
    public String showCreateUserForm(Model model) {
        model.addAttribute("newUser", new User());
        return "admin/user/create";
    }

    @PostMapping("/admin/user/create")
    public String createUserPage(Model model,
            @ModelAttribute("newUser") User newUser,
            @RequestParam("role.name") String roleName) {

        // Tìm vai trò theo tên trong cơ sở dữ liệu
        Role role = roleService.findByName(roleName); // Tìm vai trò trong DB

        newUser.setRole(role);

        userService.handleSaveUser(newUser);

        return "hello"; // Chuyển hướng đến trang danh sách người dùng
    }

    @RequestMapping("/admin/user/{id}")
    public String getUserDetailPage(Model model, @PathVariable long id) {
        User user = this.userService.getUserById(id);
        model.addAttribute("user", user);
        return "admin/user/detail";
    }

    @GetMapping("/admin/user/update/{id}")
    public String updateUserPage(Model model, @PathVariable long id) {
        User currentUser = this.userService.getUserById(id);
        model.addAttribute("updateUser", currentUser);
        return "admin/user/update";
    }

    @PostMapping(value = "/admin/user/update")
    public String updateUser(Model model, @ModelAttribute("updateUser") User updateUser) {
        User currentUser = this.userService.getUserById(updateUser.getUserId());
        currentUser.setIntroduce(updateUser.getIntroduce());
        currentUser.setName(updateUser.getName());
        userService.handleSaveUser(currentUser);
        return "redirect:/admin/user";
    }

    @GetMapping("/admin/user/delete/{id}")
    public String deleteUser(Model model, @PathVariable long id) {
        User delUser = new User();
        delUser.setUserId(id);
        model.addAttribute("delUser", delUser);
        return "admin/user/delete";
    }

    @PostMapping("/admin/user/delete")
    public String getMethodName(@ModelAttribute("delUser") User delUser) {
        this.userService.removeById(delUser.getUserId());
        return "redirect:/admin/user";
    }
}
