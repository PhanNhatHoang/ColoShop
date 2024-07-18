package com.web.coloshop.Controller;

import com.web.coloshop.Service.CartItemService;
import com.web.coloshop.Service.CartService;
import com.web.coloshop.Service.OrderService;
import com.web.coloshop.Service.StatusService;
import com.web.coloshop.model.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class OrderController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private CartService cartService;
    @Autowired
    private CartItemService cartItemService;
    @Autowired
    private StatusService statusService;
    @GetMapping("/managerOrder")
    public String managerOrder(Model model, HttpSession session){
        List<Order> orders = orderService.findByUserId((Long) session.getAttribute("userID"));
        model.addAttribute("orders", orders);
        if (session != null && session.getAttribute("userID") != null) {
            Long userId = (Long) session.getAttribute("userID");
            Cart cart = cartService.findbyUserId(userId);
            Long cartId = cart.getId();
            int count = cartItemService.getCountByCartId(cartId);
            model.addAttribute("count", count);
        } else {
            model.addAttribute("count", 0);
        }
        return "User/ManagerOrder";
    }
    @RequestMapping(value = "/UpdateOrderUser",method ={RequestMethod.PUT, RequestMethod.GET})
    public String updateOrderUser(Long id , Model model){
        Order order = orderService.findById(id);
        if (order.getStatus().getId() == 1){
            Status status = statusService.findById(2L);
            order.setStatus(status);
            orderService.save(order);
        }else {
            Status status = statusService.findById(5L);
            order.setStatus(status);
            orderService.save(order);
        }
        return "redirect:/managerOrder";
    }

    @GetMapping("/admin/managerOrder")
    public String admin_managerOrder(Model model,HttpSession session){
        String role = (String) session.getAttribute("role");
        if (role != null && role.equals("Admin")){
        List<Order> orders = orderService.findAll();
        model.addAttribute("orders",orders);
        List<Status> statuses = statusService.findAll();
        model.addAttribute("statuses",statuses);
            return "Admin/ManagerOrder";
        }else {
            return "redirect:/404";
        }

    }
    @PostMapping("/UpdateStatus")
    public String updateUserRole(@RequestParam("orderId") Long orderId,
                                 @RequestParam("statusId") Long statusId, RedirectAttributes attributes){
        Order order = orderService.findById(orderId);
        Status status = statusService.findById(statusId);
        order.setStatus(status);
        orderService.save(order);
        return "redirect:/admin/managerOrder";
    }
}
