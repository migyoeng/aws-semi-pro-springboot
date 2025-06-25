package com.example.project.repository;

import com.example.project.entity.Stack;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface StackRepository extends JpaRepository<Stack, Long> {
    
    Optional<Stack> findByName(String name);
    
    List<Stack> findAllByOrderByNameAsc();
    
    @Query("SELECT s FROM Stack s WHERE LOWER(s.name) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Stack> findByNameContainingIgnoreCase(@Param("keyword") String keyword);
} 