package com.web.coloshop.Controller;

import com.web.coloshop.Service.*;
import com.web.coloshop.model.Brand;
import com.web.coloshop.model.Cart;
import com.web.coloshop.model.Category;
import com.web.coloshop.model.Product;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.imageio.IIOException;
import java.awt.print.Pageable;
import java.io.IOException;
import java.util.List;
import java.util.Base64;

@Controller
public class ProductController {

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private BrandService brandService;
    @Autowired
    private ProductService productService;
    @Autowired
    private CartService cartService;
    @Autowired
    private CartItemService cartItemService;

    @GetMapping("/products")
    public String listProduct(Model model, HttpSession session) {
        String role = (String) session.getAttribute("role");
        if (role != null && role.equals("Admin")){
            List<Product> products = productService.findAll();
            model.addAttribute("products", products);
            return "Admin/product";
        }else {
            return "redirect:/404";
        }

    }
    @GetMapping("/Admin/Add_Product")
    public String Addproduct(Model model, HttpSession session){
        String role = (String) session.getAttribute("role");
        if (role != null && role.equals("Admin")){
            List<Category> categories = categoryService.findAllByActivated();
            List<Brand> brands = brandService.findAllByActivated();
            model.addAttribute("categories", categories);
            model.addAttribute("brands", brands);
            return "Admin/addProduct";
        }else {
            return "redirect:/404";
        }

    }
    @PostMapping("/Admin/Save_Product")
    public String saveProduct(@ModelAttribute Product product, RedirectAttributes attributes, MultipartFile imageProduct)throws IOException {
        try{

        product.setImage(Base64.getEncoder().encodeToString(imageProduct.getBytes()));
        product.set_activated(true);
        product.set_deleted(false);
        productService.save(product);
        return "redirect:/products";
        }catch (IIOException e) {
            // Xử lý ngoại lệ IOException
            // Ví dụ: ghi log, hiển thị thông báo lỗi cho người dùng, vv.
            return "error"; // Hoặc chuyển hướng đến trang lỗi
        }
    }
    @GetMapping("/Admin/Update_Product/{id}")
    public String showUpdateForm(@PathVariable Long id, Model model,HttpSession session){
        String role = (String) session.getAttribute("role");
        if (role != null && role.equals("Admin")){
            List<Category> categories = categoryService.findAllByActivated();
            List<Brand> brands = brandService.findAllByActivated();
            Product product = productService.findById(id);
            model.addAttribute("categories", categories);
            model.addAttribute("brands", brands);
            model.addAttribute("product", product);
            return "Admin/UpdateProduct";
        }else {
            return "redirect:/404";
        }

    }
    @PostMapping("/Admin/Update_Product/{id}")
    public String UpdateProduct(@PathVariable Long id,
                                @ModelAttribute Product product,
                                RedirectAttributes attributes,
                                MultipartFile imageProduct)throws IOException {
        try{

            product.setImage(Base64.getEncoder().encodeToString(imageProduct.getBytes()));
            product.set_activated(true);
            product.set_deleted(false);
            productService.save(product);
            return "redirect:/products";
        }catch (IIOException e) {
            // Xử lý ngoại lệ IOException
            // Ví dụ: ghi log, hiển thị thông báo lỗi cho người dùng, vv.
            return "error"; // Hoặc chuyển hướng đến trang lỗi
        }
    }
    @RequestMapping(value = "/delete-Product", method = {RequestMethod.PUT, RequestMethod.GET})
    public String delete(Long id,Model model){
        try {
            productService.deleteById(id);
        }catch (Exception e){
            e.printStackTrace();
        }
        return "redirect:/products";
    }

    @RequestMapping(value = "/enable-Product", method = {RequestMethod.PUT, RequestMethod.GET})
    public String enable(Long id,Model model){
        try {
            productService.enabledById(id);
        }catch (Exception e){
            e.printStackTrace();
        }
        return "redirect:/products";
    }
    //Customer
    @GetMapping("/shop")
    public String Shop(Model  model, HttpSession session){
        List<Category> categories = categoryService.findAllByActivated();
        model.addAttribute("categories",categories);
        List<Product> products = productService.getAllProducts();
        model.addAttribute("products", products);
        if (session != null && session.getAttribute("userID") != null) {
            Long userId = (Long) session.getAttribute("userID");
            Cart cart = cartService.findbyUserId(userId);
            Long cartId = cart.getId();
            int count = cartItemService.getCountByCartId(cartId);
            model.addAttribute("count", count);
        } else {
            model.addAttribute("count", 0);
        }
        return "/user/Shop";
    }

    @GetMapping("/shop/Category/{id}")
    public String findtoCategory(@PathVariable Long id, Model model, HttpSession session){
        List<Product> products = productService.findByCategoryId(id);
        List<Category> categories = categoryService.findAllByActivated();
        model.addAttribute("categories",categories);
        model.addAttribute("products", products);
        model.addAttribute("Categoryid", id);
        if (session != null && session.getAttribute("userID") != null) {
            Long userId = (Long) session.getAttribute("userID");
            Cart cart = cartService.findbyUserId(userId);
            Long cartId = cart.getId();
            int count = cartItemService.getCountByCartId(cartId);
            model.addAttribute("count", count);
        } else {
            model.addAttribute("count", 0);
        }
        return "/user/ShopInCategory";
    }

    @GetMapping("/ProductDetail")
    public String ProductDetail(Long id,Model  model, HttpSession session){
        Product products = productService.findById(id);
        model.addAttribute("products", products);
        if (session != null && session.getAttribute("userID") != null) {
            Long userId = (Long) session.getAttribute("userID");
            Cart cart = cartService.findbyUserId(userId);
            Long cartId = cart.getId();
            int count = cartItemService.getCountByCartId(cartId);
            model.addAttribute("count", count);
        } else {
            model.addAttribute("count", 0);
        }
        return "/user/productDetail";
    }

}
