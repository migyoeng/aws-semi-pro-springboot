package com.example.project.controller;

import com.example.project.entity.Stack;
import com.example.project.service.StackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/stacks")
@CrossOrigin(origins = "*")
public class StackController {
    
    @Autowired
    private StackService stackService;
    
    @GetMapping
    public ResponseEntity<List<Stack>> getAllStacks() {
        return ResponseEntity.ok(stackService.getAllStacks());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Stack> getStackById(@PathVariable Long id) {
        return stackService.getStackById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<Stack> createStack(@RequestBody Stack stack) {
        return ResponseEntity.ok(stackService.saveStack(stack));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Stack> updateStack(@PathVariable Long id, @RequestBody Stack stack) {
        try {
            Stack updatedStack = stackService.updateStack(id, stack);
            return ResponseEntity.ok(updatedStack);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStack(@PathVariable Long id) {
        stackService.deleteStack(id);
        return ResponseEntity.ok().build();
    }
} 