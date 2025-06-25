package com.example.project.service;

import com.example.project.entity.Project;
import com.example.project.entity.Stack;
import com.example.project.repository.ProjectRepository;
import com.example.project.repository.StackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProjectService {
    
    @Autowired
    private ProjectRepository projectRepository;
    
    @Autowired
    private StackRepository stackRepository;
    
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
        // 스택 처리
        if (project.getStacks() != null) {
            Set<Stack> processedStacks = project.getStacks().stream()
                .map(stack -> {
                    // 기존 스택이 있는지 확인
                    Optional<Stack> existingStack = stackRepository.findByName(stack.getName());
                    return existingStack.orElseGet(() -> stackRepository.save(stack));
                })
                .collect(Collectors.toSet());
            project.setStacks(processedStacks);
        }
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
            project.setProjectUrl(projectDetails.getProjectUrl());
            
            // 스택 처리
            if (projectDetails.getStacks() != null) {
                Set<Stack> processedStacks = projectDetails.getStacks().stream()
                    .map(stack -> {
                        // 기존 스택이 있는지 확인
                        Optional<Stack> existingStack = stackRepository.findByName(stack.getName());
                        return existingStack.orElseGet(() -> stackRepository.save(stack));
                    })
                    .collect(Collectors.toSet());
                project.setStacks(processedStacks);
            }
            
            return projectRepository.save(project);
        }
        throw new RuntimeException("Project not found with id: " + id);
    }
} 