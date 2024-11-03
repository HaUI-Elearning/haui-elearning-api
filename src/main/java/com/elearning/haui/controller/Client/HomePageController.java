package com.elearning.haui.controller.Client;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomePageController {

    @GetMapping("/")
    public String getHomePage() {
        return "hello";
    }

    @GetMapping("/access-deny")
    public String getDenyPage(Model model) {
        return "client/auth/401";
    }

}
