package com.elearning.haui.controller.Admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {
    @GetMapping("/admin")
    public String getDashboard() {
        return "/admin/dashboard/charts";
    }
}