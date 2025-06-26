package com.example.project.controller;

import com.example.project.entity.MetricData;
import com.example.project.service.MetricService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {
    
    @Autowired
    private MetricService metricService;

    private boolean isAdminAuthed(HttpServletRequest request) {
        Object flag = request.getSession().getAttribute("isAdminAuthed");
        return Boolean.TRUE.equals(flag);
    }
    
    // 대시보드 메인 페이지
    @GetMapping
    public String dashboard(Model model, HttpServletRequest request) {
        if (!isAdminAuthed(request)) {
            // 인증 안 된 경우 admin 인증 페이지로 리다이렉트(혹은 인증 모달)
            return "redirect:/admin";
        }
        Map<String, List<MetricData>> dashboardData = metricService.getDashboardData();
        model.addAttribute("dashboardData", dashboardData);
        return "dashboard";
    }
    
    // 메트릭 수집 API (수동 트리거)
    @PostMapping("/collect-metrics")
    @ResponseBody
    public ResponseEntity<String> collectMetrics(HttpServletRequest request) {
        if (!isAdminAuthed(request)) {
            return ResponseEntity.status(403).body("관리자 인증 필요");
        }
        try {
            metricService.collectAllMetrics();
            return ResponseEntity.ok("메트릭 수집 완료");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("메트릭 수집 실패: " + e.getMessage());
        }
    }
    
    // 특정 리소스의 최신 메트릭 조회 API
    @GetMapping("/api/metrics/{resourceType}/{resourceName}")
    @ResponseBody
    public ResponseEntity<List<MetricData>> getMetrics(
            @PathVariable String resourceType,
            @PathVariable String resourceName,
            HttpServletRequest request) {
        if (!isAdminAuthed(request)) {
            return ResponseEntity.status(403).build();
        }
        try {
            List<MetricData> metrics = metricService.getLatestMetrics(resourceType, resourceName);
            return ResponseEntity.ok(metrics);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    // 실시간 메트릭 업데이트 API
    @GetMapping("/api/refresh")
    @ResponseBody
    public ResponseEntity<Map<String, List<MetricData>>> refreshMetrics(HttpServletRequest request) {
        if (!isAdminAuthed(request)) {
            return ResponseEntity.status(403).build();
        }
        try {
            // 새로운 메트릭 수집
            metricService.collectAllMetrics();
            
            // 최신 데이터 반환
            Map<String, List<MetricData>> dashboardData = metricService.getDashboardData();
            return ResponseEntity.ok(dashboardData);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
} 