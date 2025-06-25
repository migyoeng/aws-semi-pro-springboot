package com.example.project.repository;

import com.example.project.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    
    // 특정 스택을 사용하는 프로젝트들 조회
    @Query("SELECT DISTINCT p FROM Project p JOIN p.stacks s WHERE s.name = :stackName")
    List<Project> findByStackName(@Param("stackName") String stackName);
    
    // 모든 프로젝트를 생성일 기준 내림차순으로 조회
    List<Project> findAllByOrderByCreatedAtDesc();
} 