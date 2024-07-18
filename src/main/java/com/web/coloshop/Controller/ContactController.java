package com.web.coloshop.Controller;

import com.web.coloshop.Service.ContactService;
import com.web.coloshop.model.Contact;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class ContactController {
    @Autowired
    private ContactService contactService;
    @GetMapping("/contacts")
    public String Contact(){
        return "/User/contact";
    }
    @PostMapping("/Save_Contact")
    public String saveContact(@ModelAttribute Contact contact, RedirectAttributes attributes){
        contactService.save(contact);
        return "redirect:/contacts";
    }
    @GetMapping("/Admin/Contacts")
    public String manager_Contact (Model model , HttpSession session){
        String role = (String) session.getAttribute("role");
        if (role != null && role.equals("Admin")){
            List<Contact> contacts = contactService.findAll();
            model.addAttribute("contacts", contacts);
            return"/Admin/ManagerContact";
        }else {
            return "redirect:/404";
        }

    }
    @DeleteMapping("/delete-Contacts/{ContactsId}")
    public String deleteContacts(@PathVariable("ContactsId") Long ContactsId,HttpSession session) {
        String role = (String) session.getAttribute("role");
        if (role != null && role.equals("Admin")){
            contactService.Delete(ContactsId);
            return"/Admin/ManagerContact";
        }else {
            return "redirect:/404";
        }


    }
}

