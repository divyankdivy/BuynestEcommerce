package com.myecom.myecomapp.controller;

import com.myecom.myecomapp.model.Category;
import com.myecom.myecomapp.model.Product;
import com.myecom.myecomapp.model.User;
import com.myecom.myecomapp.service.CategoryService;
import com.myecom.myecomapp.service.ProductService;
import com.myecom.myecomapp.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private CategoryService catService;

    @Autowired
    private ProductService prodService;

    @Autowired
    private UserService userService;

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

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @GetMapping("/products")
    public String products(@RequestParam(value = "category", required = false) Integer categoryId, Model model) {

        List<Category> categories = catService.getAllCategories();
        model.addAttribute("categories", categories);

        List<Product> products = prodService.getAllActiveProducts();

        if (categoryId != null) {
            products = prodService.getProductsByCategory(categoryId);
        }

        model.addAttribute("products", products);
        model.addAttribute("selectedCategoryId", categoryId);

        return "products";
    }

    @GetMapping("/product-view/{product_id}")
    public String productView(@PathVariable int product_id, Model model) {

        Product product = prodService.getProductById(product_id);
        model.addAttribute("product", product);

        return "view-product";
    }

//    *************************************************************************************
//    ******************************** User Mapping ***************************************
//    *************************************************************************************

    @PostMapping("/saveUser")
    public String saveUser(@ModelAttribute User user, @RequestParam("confirm-password") String confirmPassword, HttpSession session) {

        if (!user.getPassword().equals(confirmPassword)) {
            session.setAttribute("errorMsg", "Passwords do not match");
            return "redirect:/register";
        }

        userService.saveUser(user);
        session.setAttribute("successMsg", "User Saved Successfully");
        return "redirect:/login";
    }

}
