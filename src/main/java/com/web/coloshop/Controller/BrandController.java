package com.web.coloshop.Controller;

import com.web.coloshop.Service.BrandService;
import com.web.coloshop.model.Brand;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class BrandController {
    @Autowired
    private BrandService brandservice;

    @GetMapping("/brands")
    public String listBrands(Model model, HttpSession session){
        String role = (String) session.getAttribute("role");
        if (role != null && role.equals("Admin")){
            List<Brand> brands = brandservice.findAll();
            model.addAttribute("brands", brands);
            return "/Admin/Brand";
        }else {
            return "redirect:/404";
        }

    }

    @PostMapping("/saveBrand")
    public String addOrUpdateBrand(@ModelAttribute Brand brand , RedirectAttributes attributes){
        try {
            Brand existingBrand = brandservice.findByName(brand.getName());
            if(existingBrand == null){
                //Nếu không tồn tại, kiểm tra id
                if(brand.getId() != null){
                    // Nếu có id, đây là một trường hợp edit
                    Brand existingBrandByID = brandservice.findById(brand.getId());
                    if(existingBrandByID != null){
                        // Nếu tồn tại theo id, thực hiện edit
                        existingBrandByID.setName(brand.getName());
                        brandservice.save(existingBrandByID);
                    }else {
                        attributes.addFlashAttribute("error", "Brand with the specified ID not found.");
                    }
                }else {
                    brand.set_activated(true);
                    brand.set_deleted(false);
                    brandservice.save(brand);
                }
            }else {
                attributes.addFlashAttribute("error", "Brand Name with the same name already exists.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            attributes.addFlashAttribute("error", "An error occurred while processing the request.");
        }
        return "redirect:/brands";
    }
    @RequestMapping(value = "/delete-brand", method = {RequestMethod.PUT, RequestMethod.GET})
    public String delete(Long id,Model model){
        try {
            brandservice.deleteById(id);
        }catch (Exception e){
            e.printStackTrace();
        }
        return "redirect:/brands";
    }

    @RequestMapping(value = "/enable-brand", method = {RequestMethod.PUT, RequestMethod.GET})
    public String enable(Long id,Model model){
        try {
            brandservice.enabledById(id);
        }catch (Exception e){
            e.printStackTrace();
        }
        return "redirect:/brands";
    }
}
