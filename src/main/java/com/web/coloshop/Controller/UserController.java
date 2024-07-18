package com.web.coloshop.Controller;

import com.web.coloshop.Service.CartItemService;
import com.web.coloshop.Service.CartService;
import com.web.coloshop.Service.RoleService;
import com.web.coloshop.Service.UserService;
import com.web.coloshop.model.Cart;
import com.web.coloshop.model.Role;
import com.web.coloshop.model.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.Console;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.math.BigInteger;
import java.util.List;


@Controller
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private CartService cartService;
    @Autowired
    private CartItemService cartItemService;
    @GetMapping("/SignUp")
    public String SignUp(HttpSession session) {
        if (session !=null && session.getAttribute("user") != null){
            return "redirect:/shop";
        }else {
            return "Admin/Sign_Up";
        }
    }
    public static String md5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(input.getBytes());
            byte[] digest = md.digest();
            BigInteger bigInteger = new BigInteger(1, digest);
            String hashText = bigInteger.toString(16);
            while (hashText.length() < 32) {
                hashText = "0" + hashText;
            }
            return hashText;
        } catch (NoSuchAlgorithmException e) {
            // Xử lý nếu thuật toán MD5 không được hỗ trợ
            e.printStackTrace();
            return null;
        }
    }
    @PostMapping("/save_SignUp")
    public String saveSignUp(@ModelAttribute User user, @RequestParam("firstName") String firstName, @RequestParam("lastName") String lastName, RedirectAttributes attributes){

        user.setFullname(firstName + " " + lastName);
        Role role = roleService.findById(2L); // Giả sử roleService cung cấp phương thức findById

        User emailExists = userService.findByEmail(user.getEmail());
        if (emailExists != null) {
            attributes.addFlashAttribute("error", "Email Exists");
            return "redirect:/SignUp";
        }else {
            user.setRole(role);
            user.setPass(md5(user.getPass()));
            user.set_activated(true);
            user.set_deleted(false);
            userService.save(user);
        }
        return "redirect:/Login";
    }
    @GetMapping("/Login")
    public String Login(HttpSession session) {
        if (session !=null && session.getAttribute("user") != null){
            return "redirect:/shop";
        }else {
            return "Admin/Login";
        }

    }
    @PostMapping("/Do_Login")
    public String DoLogin(@RequestParam("email") String email,@RequestParam("pass") String password, HttpSession session,RedirectAttributes attributes){
        User user = userService.findByEmail(email);
        String PassDaMaHoa = md5(password);
        if(user !=null){
            if(user.getPass().equals(PassDaMaHoa)){
                session.setAttribute("user",user);
                session.setAttribute("role",user.getRole().getName());
                session.setAttribute("userID",user.getId());
                Long UserID = user.getId();
                Cart cart = cartService.findbyUserId(UserID);
                if (cart == null){
                    cart = new Cart();
                    cart.setUser(user);
                    cartService.save(cart);
                }
                return "redirect:/shop";
            }else {
                attributes.addFlashAttribute("error", "Wrong Password");
                return "redirect:/Login";
            }
        }else {
            attributes.addFlashAttribute("error", "Wrong email address");
            return "redirect:/Login";
        }
    }
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        // Xóa thuộc tính người dùng khỏi session khi đăng xuất
        session.removeAttribute("user");
        // Sau đó, chuyển hướng người dùng đến trang đăng nhập hoặc trang chính của ứng dụng
        return "redirect:/Login";
    }
    @GetMapping("/404")
    public String erro_404(){
        return "/Admin/404";
    }
    @GetMapping("/Admin/Manager_User")
    public String listManager_User(Model model, HttpSession session){
        String role = (String) session.getAttribute("role");
        if (role != null && role.equals("Admin")){
            List<User> users = userService.findAll();
            List<Role> roles= roleService.findAll();
            model.addAttribute("roles",roles);
            model.addAttribute("users", users);
            return "/Admin/ManagerUser";
        }else {
                return "redirect:/404";
        }

    }
    @PostMapping("/Update_User_Role")
    public String updateUserRole(@RequestParam("userId") Long userId,
                                 @RequestParam("role") Long roleId, RedirectAttributes attributes){
        User user = userService.findById(userId);
        Role role = roleService.findById(roleId);
        user.setRole(role);
        userService.save(user);
         return "redirect:/Admin/Manager_User";
    }
    @RequestMapping(value = "/delete-User", method = {RequestMethod.PUT, RequestMethod.GET})
    public String delete(Long id,Model model){
        try {
            userService.deleteById(id);
        }catch (Exception e){
            e.printStackTrace();
        }
        return "redirect:/Admin/Manager_User";
    }

    @RequestMapping(value = "/enable-User", method = {RequestMethod.PUT, RequestMethod.GET})
    public String enable(Long id,Model model){
        try {
            userService.enabledById(id);
        }catch (Exception e){
            e.printStackTrace();
        }
        return "redirect:/Admin/Manager_User";
    }
    @GetMapping("/Info")
    public String Info(Model model, HttpSession session){
        User users = userService.findById((Long) session.getAttribute("userID"));
        if (session != null && session.getAttribute("userID") != null) {
            Long userId = (Long) session.getAttribute("userID");
            Cart cart = cartService.findbyUserId(userId);
            Long cartId = cart.getId();
            int count = cartItemService.getCountByCartId(cartId);
            model.addAttribute("count", count);
        } else {
            model.addAttribute("count", 0);
        }
        model.addAttribute("users", users);
        return "/User/customer";
    }
    @PostMapping("/saveInfo")
    public String saveInfo(@ModelAttribute User user , HttpSession session){
        User infoUser = userService.findById((Long) session.getAttribute("userID"));
        infoUser.setAddress(user.getAddress());
        infoUser.setPhone(user.getPhone());
        userService.save(infoUser);
        return "redirect:/Cart";
    }

}
