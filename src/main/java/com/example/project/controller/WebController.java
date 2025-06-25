package com.example.project.controller;

import com.example.project.service.ProjectService;
import com.example.project.service.StackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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
} 