package com.web.coloshop.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class dashboard {
    @GetMapping("/dashboard")
    public String dashboard(){
        return "admin/dashboard";
    }
    @GetMapping("/dashboard1")
    public String dashboard1(){
        return "User/dashboard1";
    }
}
