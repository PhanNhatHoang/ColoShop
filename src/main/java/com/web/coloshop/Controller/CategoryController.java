package com.web.coloshop.Controller;

import com.web.coloshop.Repository.CategoryRepository;
import com.web.coloshop.Service.CategoryService;
import com.web.coloshop.model.Category;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.thymeleaf.model.IModel;

import java.util.List;
@Controller
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping("/categories")
    public String listCategories(Model model, HttpSession session) {
        String role = (String) session.getAttribute("role");
        if (role != null && role.equals("Admin")){
            List<Category> categories = categoryService.findAll();
            model.addAttribute("categories", categories);
            return "Admin/Category";
        }else {
            return "redirect:/404";
        }

    }

   @PostMapping("/saveCategory")
   public String addOrUpdateCategory(@ModelAttribute Category category, RedirectAttributes attributes) {
       try {
        Category existingCategory = categoryService.findByName(category.getName());

         if (existingCategory == null) {
           // Nếu không tồn tại, kiểm tra id
           if (category.getId() != null) {
               // Nếu có id, đây là một trường hợp edit
               Category existingCategoryById = categoryService.findById(category.getId());

               if (existingCategoryById != null) {
                   // Nếu tồn tại theo id, thực hiện edit
                   existingCategoryById.setName(category.getName());
                   // Cập nhật các trường khác nếu cần thiết
                   categoryService.save(existingCategoryById);
               } else {
                   // Nếu không tồn tại theo id, xử lý lỗi hoặc thông báo
                   attributes.addFlashAttribute("error", "Category with the specified ID not found.");
               }
           } else {
               category.set_activated(true);
               category.set_deleted(true);
               categoryService.save(category);
           }
       } else {
           // Nếu tồn tại theo name, xử lý lỗi hoặc thông báo
             attributes.addFlashAttribute("error", "Category Name with the same name already exists.");
       }
       } catch (Exception e) {
           e.printStackTrace();
           attributes.addFlashAttribute("error", "An error occurred while processing the request.");
       }


       return "redirect:/categories";
   }
    @RequestMapping(value = "/delete-category", method = {RequestMethod.PUT, RequestMethod.GET})
    public String delete(Long id,Model model){
        try {
            categoryService.deleteById(id);
        }catch (Exception e){
            e.printStackTrace();
        }
        return "redirect:/categories";
    }

    @RequestMapping(value = "/enable-category", method = {RequestMethod.PUT, RequestMethod.GET})
    public String enable(Long id,Model model){
        try {
            categoryService.enabledById(id);
        }catch (Exception e){
            e.printStackTrace();
        }
        return "redirect:/categories";
    }

}
