package com.elearning.haui.controller.Admin;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.ui.Model;

import com.elearning.haui.domain.entity.Course;
import com.elearning.haui.service.CourseService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class CourseController {
    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping("/admin/product")
    public String getProduct(Model model, @RequestParam(defaultValue = "1") int page) {
        int pageSize = 10;
        Page<Course> courses = this.courseService.findPaginated(PageRequest.of(page - 1, pageSize));
        model.addAttribute("listProduct", courses.getContent());
        model.addAttribute(("currentPage"), page);
        model.addAttribute("totalPages", courses.getTotalPages());
        return "/admin/product/show";
    }

    @GetMapping("/admin/product/{id}")
    public String getProductById(Model model, @PathVariable Long id) {
        Course course = this.courseService.getCourseById(id);
        model.addAttribute("course", course);
        return "/admin/product/detail";
    }

    // Create
    @GetMapping("/admin/product/create")
    public String getCreateProduct(Model model) {
        model.addAttribute("newProduct", new Course());
        return "/admin/product/create";
    }

    @PostMapping("/admin/product/create")
    public String postCreateProduct(Model model, @ModelAttribute("newProduct") Course course) {
        this.courseService.handleSaveProduct(course);
        return "redirect:/admin/product";
    }

    // Delete
    @GetMapping("/admin/product/delete/{id}")
    public String getDeleteProduct(Model model, @PathVariable long id) {
        model.addAttribute("id", id);
        model.addAttribute("deleteProduct", new Course());
        return "admin/product/delete";
    }

    @PostMapping("/admin/product/delete")
    public String postDeleteProduct(@ModelAttribute("deleteProduct") Course course) {
        this.courseService.deleteCourseById(course.getCourseId());
        return "redirect:/admin/product";
    }

    // Update
    @GetMapping("/admin/product/update/{id}")
    public String getUpdateCourse(Model model, @PathVariable Long id) {
        Course currentCourse = this.courseService.getCourseById(id);
        model.addAttribute("newProduct", currentCourse);
        return "admin/product/update";
    }

    @PostMapping(value = "/admin/product/update")
    public String updateUser(Model model, @ModelAttribute("newProduct") Course course) {
        Course currenCourse = this.courseService.getCourseById(course.getCourseId());
        if (currenCourse != null) {
            currenCourse.setName(course.getName());
            currenCourse.setThumbnail(course.getThumbnail());
            currenCourse.setDescription(course.getDescription());
            currenCourse.setContents(course.getContents());
            currenCourse.setHour(course.getHour());
            currenCourse.setPrice(course.getPrice());
            currenCourse.setAuthor(course.getAuthor());
            this.courseService.handleSaveProduct(currenCourse);
        }
        return "redirect:/admin/product";
    }
}
