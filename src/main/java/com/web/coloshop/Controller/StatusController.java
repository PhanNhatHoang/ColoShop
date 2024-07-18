package com.web.coloshop.Controller;

import com.web.coloshop.Service.StatusService;
import com.web.coloshop.model.Status;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class StatusController {
    @Autowired
    private StatusService statusService;

    @GetMapping("/Admin/Status")
    public String ListStatus(Model model, HttpSession session){
        String role = (String) session.getAttribute("role");
        if (role != null && role.equals("Admin")){
            List<Status> statusList = statusService.findAll();
            model.addAttribute("statusList", statusList);
            return "/Admin/Status";
        }else {
            return "redirect:/404";
        }

    }
    @PostMapping("/saveStatus")
    public String addOrUpdateStatus(@ModelAttribute Status status , RedirectAttributes attributes){
        try {
            Status existingStatus = statusService.findByName(status.getName());
            if(existingStatus == null){
                //Nếu không tồn tại, kiểm tra id
                if(status.getId() != null){
                    // Nếu có id, đây là một trường hợp edit
                    Status existingStatusByID = statusService.findById(status.getId());
                    if(existingStatusByID != null){
                        // Nếu tồn tại theo id, thực hiện edit
                        existingStatusByID.setName(status.getName());
                        statusService.save(existingStatusByID);
                    }else {
                        attributes.addFlashAttribute("error", "Status with the specified ID not found.");
                    }
                }else {
                    statusService.save(status);
                }
            }else {
                attributes.addFlashAttribute("error", "Status Name with the same name already exists.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            attributes.addFlashAttribute("error", "An error occurred while processing the request.");
        }
        return "redirect:/Admin/Status";
    }
}
