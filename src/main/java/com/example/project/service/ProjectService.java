package com.example.project.service;

import com.example.project.entity.Project;
import com.example.project.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ProjectService {
    
    @Autowired
    private ProjectRepository projectRepository;
    
    public List<Project> getAllProjects() {
        return projectRepository.findAllByOrderByCreatedAtDesc();
    }
    
    public List<Project> getProjectsByStackName(String stackName) {
        if ("all".equalsIgnoreCase(stackName)) {
            return getAllProjects();
        }
        return projectRepository.findByStackName(stackName);
    }
    
    public Optional<Project> getProjectById(Long id) {
        return projectRepository.findById(id);
    }
    
    public Project saveProject(Project project) {
        return projectRepository.save(project);
    }
    
    public void deleteProject(Long id) {
        projectRepository.deleteById(id);
    }
    
    public Project updateProject(Long id, Project projectDetails) {
        Optional<Project> optionalProject = projectRepository.findById(id);
        if (optionalProject.isPresent()) {
            Project project = optionalProject.get();
            project.setTitle(projectDetails.getTitle());
            project.setDescription(projectDetails.getDescription());
            project.setStacks(projectDetails.getStacks());
            project.setThumbnailUrl(projectDetails.getThumbnailUrl());
            return projectRepository.save(project);
        }
        throw new RuntimeException("Project not found with id: " + id);
    }
} 