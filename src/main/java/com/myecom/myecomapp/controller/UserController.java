package com.myecom.myecomapp.controller;

import com.myecom.myecomapp.model.User;
import com.myecom.myecomapp.service.CategoryService;
import com.myecom.myecomapp.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private CategoryService catService;

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @ModelAttribute
    public void getUserDetails(Principal p, Model model) {
        if (p != null) {
            String email = p.getName();
            User user = userService.getUserByEmail(email);
            model.addAttribute("user", user);
        }
    }

    @ModelAttribute
    public void addCategories(Model model) {
        model.addAttribute("categories", catService.getAllActiveCategories());
    }
}
