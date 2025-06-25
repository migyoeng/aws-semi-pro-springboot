package com.example.project.controller;

import com.example.project.service.ProjectService;
import com.example.project.service.StackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class WebController {
    
    @Autowired
    private ProjectService projectService;
    
    @Autowired
    private StackService stackService;
    
    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("projects", projectService.getAllProjects());
        model.addAttribute("stacks", stackService.getAllStacks());
        return "index";
    }
    
    @GetMapping("/admin")
    public String admin(Model model) {
        model.addAttribute("projects", projectService.getAllProjects());
        model.addAttribute("stacks", stackService.getAllStacks());
        return "admin";
    }

    @PostMapping("/api/admin-auth")
    @ResponseBody
    public String adminAuth() {
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attr != null) {
            attr.getRequest().getSession().setAttribute("isAdminAuthed", true);
        }
        return "ok";
    }
} 