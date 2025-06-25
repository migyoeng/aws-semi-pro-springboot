package com.example.project.controller;

import com.example.project.entity.Project;
import com.example.project.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@RestController
@RequestMapping("/api/projects")
@CrossOrigin(origins = "*")
public class ProjectController {
    
    @Autowired
    private ProjectService projectService;
    
    @GetMapping
    public ResponseEntity<List<Project>> getAllProjects() {
        return ResponseEntity.ok(projectService.getAllProjects());
    }
    
    @GetMapping("/filter/{stackName}")
    public ResponseEntity<List<Project>> getProjectsByStackName(@PathVariable String stackName) {
        return ResponseEntity.ok(projectService.getProjectsByStackName(stackName));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Project> getProjectById(@PathVariable Long id) {
        return projectService.getProjectById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    private boolean isAdminAuthed() {
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attr == null) return false;
        Object flag = attr.getRequest().getSession().getAttribute("isAdminAuthed");
        return Boolean.TRUE.equals(flag);
    }
    
    @PostMapping
    public ResponseEntity<Project> createProject(@RequestBody Project project) {
        if (!isAdminAuthed()) {
            return ResponseEntity.status(403).build();
        }
        return ResponseEntity.ok(projectService.saveProject(project));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Project> updateProject(@PathVariable Long id, @RequestBody Project project) {
        if (!isAdminAuthed()) {
            return ResponseEntity.status(403).build();
        }
        try {
            Project updatedProject = projectService.updateProject(id, project);
            return ResponseEntity.ok(updatedProject);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
        if (!isAdminAuthed()) {
            return ResponseEntity.status(403).build();
        }
        projectService.deleteProject(id);
        return ResponseEntity.ok().build();
    }
} 