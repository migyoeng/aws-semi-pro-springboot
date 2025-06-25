package com.example.project.service;

import com.example.project.entity.Stack;
import com.example.project.repository.StackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class StackService {
    
    @Autowired
    private StackRepository stackRepository;
    
    public List<Stack> getAllStacks() {
        return stackRepository.findAllByOrderByNameAsc();
    }
    
    public Optional<Stack> getStackById(Long id) {
        return stackRepository.findById(id);
    }
    
    public Optional<Stack> getStackByName(String name) {
        return stackRepository.findByName(name);
    }
    
    public Stack saveStack(Stack stack) {
        return stackRepository.save(stack);
    }
    
    public void deleteStack(Long id) {
        stackRepository.deleteById(id);
    }
    
    public Stack updateStack(Long id, Stack stackDetails) {
        Optional<Stack> optionalStack = stackRepository.findById(id);
        if (optionalStack.isPresent()) {
            Stack stack = optionalStack.get();
            stack.setName(stackDetails.getName());
            stack.setDescription(stackDetails.getDescription());
            return stackRepository.save(stack);
        }
        throw new RuntimeException("Stack not found with id: " + id);
    }
} 