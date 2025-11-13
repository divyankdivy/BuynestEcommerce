package com.myecom.myecomapp.controller;

import com.myecom.myecomapp.model.Category;
import com.myecom.myecomapp.model.Product;
import com.myecom.myecomapp.model.User;
import com.myecom.myecomapp.service.CategoryService;
import com.myecom.myecomapp.service.ProductService;
import com.myecom.myecomapp.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

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
        return "admin/index";
    }

//    *******************************************************************************
//    ********************** Mapping for Category ***********************************
//    *******************************************************************************

    @GetMapping("/add-category")
    public String addCategory(Model model) {
        model.addAttribute("categories", catService.getAllCategories());
        return "admin/add_category";
    }

    @PostMapping("/saveCategory")
    public String saveCategory(@ModelAttribute Category category, HttpSession session) {

        Boolean existCat = catService.isCategoryExist(category.getName());

        if (existCat) {
            session.setAttribute("errorMsg", "Category already exists");
        } else {
            Category saveCat = catService.saveCategory(category);
            if (ObjectUtils.isEmpty(saveCat)) {
                session.setAttribute("errorMsg", "Not Saved, Internal Server Error");
            } else {
                session.setAttribute("successMsg", "Saved Successfully");
            }
        }
        return "redirect:/admin/add-category";
    }

    @GetMapping("/delete-category/{id}")
    public String deleteCategory(@PathVariable int id, HttpSession session) {

        Boolean deleteCategory = catService.deleteCategory(id);

        if (deleteCategory) {
            session.setAttribute("successMsg", "Deleted Successfully");
        } else {
            session.setAttribute("errorMsg", "Deleted Failed");
        }

        return "redirect:/admin/add-category";
    }

    @GetMapping("/load-edit-category/{categoryId}")
    public String loadEditCategory(@PathVariable int categoryId, Model model, HttpSession session) {
        model.addAttribute("category", catService.getCategoryById(categoryId));
        return "admin/edit_category";
    }

    @PostMapping("/updateCategory")
    public String updateCategory(@ModelAttribute Category category, HttpSession session) {

        Category getOldCat = catService.getCategoryById(category.getCategoryId());

        if (!ObjectUtils.isEmpty(category)) {
            getOldCat.setName(category.getName());
            getOldCat.setIsActive(category.getIsActive());
        }
        Category updatedCat = catService.saveCategory(getOldCat);

        if (!ObjectUtils.isEmpty(updatedCat)) {
            session.setAttribute("successMsg", "Saved Successfully");
        } else {
            session.setAttribute("errorMsg", "Saved Failed");
        }

        return "redirect:/admin/add-category";
    }

//    *******************************************************************************
//    ************************ Mapping for Product **********************************
//    *******************************************************************************

    @GetMapping("/add-product")
    public String loadAddProduct(Model model) {
        List<Category> categories = catService.getAllCategories();
        model.addAttribute("categories", categories);
        return "admin/add_product";
    }

    @PostMapping("/saveProduct")
    public String saveProduct(@ModelAttribute Product product, @RequestParam("image") MultipartFile image, HttpSession session) {

        try {
            // ✅ 1. Get image name (use default if empty)
            String imageName = image.isEmpty() ? "default.jpg" : image.getOriginalFilename();
            product.setImageName(imageName);

            // ✅ 2. Save product details first
            prodService.saveProduct(product);

            // ✅ 3. If an image is uploaded, save it to the static folder
            if (!image.isEmpty()) {
                File saveFile = new ClassPathResource("static/img").getFile();
                Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + "product_img");

                // Create folder if not exists
                if (!Files.exists(path)) {
                    Files.createDirectories(path);
                }

                // Save file
                Files.copy(image.getInputStream(), path.resolve(imageName), StandardCopyOption.REPLACE_EXISTING);
            }

            session.setAttribute("successMsg", "Product added successfully!");
        } catch (Exception e) {
            System.out.println("Error while saving product: " + e.getMessage());
            session.setAttribute("errorMsg", "Something went wrong while saving product!");
        }
        return "redirect:/admin/add-product";
    }

//    *******************************************************************************
//    ************************ Mapping for View Product *****************************
//    *******************************************************************************

    @GetMapping("/viewProducts")
    public String viewProduct(Model model, HttpSession session) {
        List<Product> products = prodService.getAllProducts();
        model.addAttribute("products", products);
        return "admin/view_products";
    }

    @GetMapping("/delete-product/{productId}")
    public String deleteProduct(@PathVariable int productId, HttpSession session) {
        Boolean deleteProduct = prodService.deleteProduct(productId);

        if (deleteProduct) {
            session.setAttribute("successMsg", "Product deleted successfully!");
        } else {
            session.setAttribute("errorMsg", "Product not deleted");
        }
        return "redirect:/admin/viewProducts";
    }

    @GetMapping("/load-edit-product/{productId}")
    public String updateProduct(@PathVariable int productId, Model model, HttpSession session) {

        Product product = prodService.getProductById(productId);
        List<Category> categories = catService.getAllCategories();

        if (product != null) {
            model.addAttribute("product", product);
            model.addAttribute("categories", categories);
        } else {
            session.setAttribute("errorMsg", "Product not found");
        }
        return "admin/edit_product";
    }

    @PostMapping("/updateProduct")
    public String updateProduct(@ModelAttribute Product product, @RequestParam("image") MultipartFile imageFile, HttpSession session) {

        try {
            Product updated = prodService.updateProduct(product, imageFile);

            if (updated != null) {
                session.setAttribute("successMsg", "Product updated successfully!");
            } else {
                session.setAttribute("errorMsg", "Failed to update product.");
            }
        } catch (Exception e) {
            session.setAttribute("errorMsg", "Something went wrong while updating product!");
        }
        return "redirect:/admin/viewProducts";
    }

//    *******************************************************************************
//    ************************ Mapping for View Users *****************************
//    *******************************************************************************

    @GetMapping("/viewUsers")
    public String viewUsers(Model model, HttpSession session) {
        List<User> users = userService.getAllUsers();  // fetch all users
        model.addAttribute("users", users);
        return "user/view_users";
    }

    @GetMapping("/viewUser/{id}")
    public String viewUser(@PathVariable int id, Model model, HttpSession session) {
        User user = userService.getByUserId(id);
        model.addAttribute("user", user);
        return "user/edit_user";
    }

    @PostMapping("viewUser/update_role")
    public String updateUserRole(@RequestParam int userId, @RequestParam String role, HttpSession session) {
        userService.updateUserRole(userId, role);
        return "redirect:/admin/viewUsers";
    }

    @GetMapping("/deleteUser/{id}")
    public String deleteUser(@PathVariable int id, HttpSession session ) {

        Boolean deleteUser = userService.deleteUserById(id);

        if (deleteUser) {
            session.setAttribute("successMsg", "User deleted successfully!");
        } else {
            session.setAttribute("errorMsg", "User not deleted");
        }
        return "redirect:/admin/viewUsers";
    }
}
