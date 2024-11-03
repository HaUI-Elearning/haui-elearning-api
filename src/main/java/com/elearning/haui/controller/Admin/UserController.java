package com.elearning.haui.controller.Admin;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private final PasswordEncoder passwordEncoder;

    public UserController(UserService userService, RoleService roleService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/admin/user")
    public String viewUsersPage(Model model,
            @RequestParam(defaultValue = "1") int page) {
        int pageSize = 10; // Number of records per page
        Page<User> userPage = userService.findPaginated(PageRequest.of(page - 1, pageSize));
        model.addAttribute("users", userPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", userPage.getTotalPages());
        return "admin/user/show"; // The name of your JSP page
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

        Role role = roleService.findByName(roleName);
        newUser.setRole(role);
        newUser.setPassword(this.passwordEncoder.encode(newUser.getPassword()));
        userService.handleSaveUser(newUser);

        return "redirect:/admin/user";
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

    @PostMapping("/admin/user/update")
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
