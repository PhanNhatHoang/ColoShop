package com.web.coloshop.Controller;

import com.web.coloshop.Service.OrderDetailService;
import com.web.coloshop.model.OrderDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class OrderDetailController {
    @Autowired
    private OrderDetailService orderDetailService;

    @GetMapping("/OrderDetailUser")
    public String orderDetailId (Long OrderId, Model model){
        List<OrderDetail> orderDetails = orderDetailService.findByOrderId(OrderId);
        model.addAttribute("orderDetails", orderDetails);
        return "/User/ManagerOrderDetail";
    }

    @GetMapping("/Admin/OrderDetail")
    public String Admin_orderDetailId (Long OrderId, Model model){
        List<OrderDetail> orderDetails = orderDetailService.findByOrderId(OrderId);
        model.addAttribute("orderDetails", orderDetails);
        return "/Admin/OrderDetail";
    }

}
