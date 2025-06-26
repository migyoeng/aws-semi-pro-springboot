package com.example.project.repository;

import com.example.project.entity.MetricData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MetricDataRepository extends JpaRepository<MetricData, Long> {
    
    // 특정 리소스의 최신 메트릭 데이터 조회
    List<MetricData> findByResourceTypeAndResourceNameOrderByTimestampDesc(
        String resourceType, String resourceName);
    
    // 특정 리소스의 특정 메트릭 최신 데이터 조회
    MetricData findTopByResourceTypeAndResourceNameAndMetricNameOrderByTimestampDesc(
        String resourceType, String resourceName, String metricName);
    
    // 특정 시간 범위의 메트릭 데이터 조회
    @Query("SELECT m FROM MetricData m WHERE m.resourceType = :resourceType " +
           "AND m.resourceName = :resourceName AND m.metricName = :metricName " +
           "AND m.timestamp BETWEEN :startTime AND :endTime ORDER BY m.timestamp")
    List<MetricData> findMetricsInTimeRange(
        @Param("resourceType") String resourceType,
        @Param("resourceName") String resourceName,
        @Param("metricName") String metricName,
        @Param("startTime") LocalDateTime startTime,
        @Param("endTime") LocalDateTime endTime);
    
    // 모든 리소스 타입별 최신 메트릭 데이터 조회
    @Query("SELECT DISTINCT m.resourceType, m.resourceName, m.metricName, " +
           "m.metricValue, m.unit, m.timestamp FROM MetricData m " +
           "WHERE m.timestamp = (SELECT MAX(m2.timestamp) FROM MetricData m2 " +
           "WHERE m2.resourceType = m.resourceType AND m2.resourceName = m.resourceName " +
           "AND m2.metricName = m.metricName)")
    List<Object[]> findLatestMetricsForAllResources();
} 