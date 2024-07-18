package com.web.coloshop.Controller;

import com.web.coloshop.Service.CategoryService;
import com.web.coloshop.Service.RoleService;
import com.web.coloshop.model.Category;
import com.web.coloshop.model.Role;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class RoleController {
    @Autowired
    private RoleService roleService;

    @GetMapping("/roles")
    public String listCategories(Model model, HttpSession session) {
        String role = (String) session.getAttribute("role");
        if (role != null && role.equals("Admin")){
            List<Role> roles = roleService.findAll();
            model.addAttribute("roles", roles);
            return "Admin/Role";
        }else {
            return "redirect:/404";
        }

    }

   @PostMapping("/saveRole")
   public String addOrUpdateCategory(@ModelAttribute Role role, RedirectAttributes attributes) {
       try {
        Role existingRole = roleService.findByName(role.getName());

         if (existingRole == null) {
           // Nếu không tồn tại, kiểm tra id
           if (role.getId() != null) {
               // Nếu có id, đây là một trường hợp edit
               Role existingRoleById = roleService.findById(role.getId());

               if (existingRoleById != null) {
                   // Nếu tồn tại theo id, thực hiện edit
                   existingRoleById.setName(role.getName());
                   // Cập nhật các trường khác nếu cần thiết
                   roleService.save(existingRoleById);
               } else {
                   // Nếu không tồn tại theo id, xử lý lỗi hoặc thông báo
                   attributes.addFlashAttribute("error", "Role with the specified ID not found.");
               }
           } else {
               // Nếu không có id, đây là một trường hợp thêm mới
               roleService.save(role);
           }
       } else {
           // Nếu tồn tại theo name, xử lý lỗi hoặc thông báo
             attributes.addFlashAttribute("error", "Role Name with the same name already exists.");
       }
       } catch (Exception e) {
           e.printStackTrace();
           attributes.addFlashAttribute("error", "An error occurred while processing the request.");
       }


       return "redirect:/roles";
   }

}
