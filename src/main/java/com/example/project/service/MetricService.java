package com.example.project.service;

import com.example.project.entity.MetricData;
import com.example.project.repository.MetricDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.cloudwatch.CloudWatchClient;
import software.amazon.awssdk.services.cloudwatch.model.*;
import software.amazon.awssdk.regions.Region;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MetricService {
    
    @Autowired
    private MetricDataRepository metricDataRepository;
    
    private final CloudWatchClient cloudWatchClient;
    private final String region = "ap-northeast-2";
    
    public MetricService() {
        this.cloudWatchClient = CloudWatchClient.builder()
            .region(Region.AP_NORTHEAST_2)
            .build();
    }
    
    // EC2 메트릭 조회
    public List<MetricData> getEC2Metrics(String instanceId) {
        List<MetricData> metrics = new ArrayList<>();
        
        // CPUUtilization
        metrics.add(getMetric("AWS/EC2", "CPUUtilization", "Percent", 
            Collections.singletonMap("InstanceId", instanceId), "EC2", instanceId));
        
        // NetworkIn
        metrics.add(getMetric("AWS/EC2", "NetworkIn", "Bytes", 
            Collections.singletonMap("InstanceId", instanceId), "EC2", instanceId));
        
        // NetworkOut
        metrics.add(getMetric("AWS/EC2", "NetworkOut", "Bytes", 
            Collections.singletonMap("InstanceId", instanceId), "EC2", instanceId));
        
        return metrics.stream().filter(Objects::nonNull).collect(Collectors.toList());
    }
    
    // RDS 메트릭 조회
    public List<MetricData> getRDSMetrics(String dbInstanceId) {
        List<MetricData> metrics = new ArrayList<>();
        
        // CPUUtilization
        metrics.add(getMetric("AWS/RDS", "CPUUtilization", "Percent", 
            Collections.singletonMap("DBInstanceIdentifier", dbInstanceId), "RDS", dbInstanceId));
        
        // DatabaseConnections
        metrics.add(getMetric("AWS/RDS", "DatabaseConnections", "Count", 
            Collections.singletonMap("DBInstanceIdentifier", dbInstanceId), "RDS", dbInstanceId));
        
        return metrics.stream().filter(Objects::nonNull).collect(Collectors.toList());
    }
    
    // ALB 메트릭 조회
    public List<MetricData> getALBMetrics(String loadBalancerName) {
        List<MetricData> metrics = new ArrayList<>();
        
        // RequestCount
        metrics.add(getMetric("AWS/ApplicationELB", "RequestCount", "Count", 
            Collections.singletonMap("LoadBalancer", loadBalancerName), "ALB", loadBalancerName));
        
        return metrics.stream().filter(Objects::nonNull).collect(Collectors.toList());
    }
    
    // Target Group 메트릭 조회
    public List<MetricData> getTargetGroupMetrics(String targetGroupName) {
        List<MetricData> metrics = new ArrayList<>();
        
        // UnhealthyHostCount
        metrics.add(getMetric("AWS/ApplicationELB", "UnHealthyHostCount", "Count", 
            Collections.singletonMap("TargetGroup", targetGroupName), "TG", targetGroupName));
        
        // TargetResponseTime
        metrics.add(getMetric("AWS/ApplicationELB", "TargetResponseTime", "Seconds", 
            Collections.singletonMap("TargetGroup", targetGroupName), "TG", targetGroupName));
        
        // HealthyHostCount
        metrics.add(getMetric("AWS/ApplicationELB", "HealthyHostCount", "Count", 
            Collections.singletonMap("TargetGroup", targetGroupName), "TG", targetGroupName));
        
        // RequestCount
        metrics.add(getMetric("AWS/ApplicationELB", "RequestCount", "Count", 
            Collections.singletonMap("TargetGroup", targetGroupName), "TG", targetGroupName));
        
        return metrics.stream().filter(Objects::nonNull).collect(Collectors.toList());
    }
    
    // 모든 메트릭 조회 및 저장
    public void collectAllMetrics() {
        try {
            // EC2 메트릭 (Auto Scaling Group의 인스턴스들)
            List<MetricData> ec2Metrics = getEC2Metrics("tempEC2PUB");
            metricDataRepository.saveAll(ec2Metrics);
            
            // RDS 메트릭
            List<MetricData> rdsMetrics = getRDSMetrics("tempRDS");
            metricDataRepository.saveAll(rdsMetrics);
            
            // ALB 메트릭
            List<MetricData> albMetrics = getALBMetrics("tempALB");
            metricDataRepository.saveAll(albMetrics);
            
            // Target Group 메트릭
            List<MetricData> tgMetrics = getTargetGroupMetrics("tempTG");
            metricDataRepository.saveAll(tgMetrics);
            
        } catch (Exception e) {
            System.err.println("메트릭 수집 중 오류 발생: " + e.getMessage());
        }
    }
    
    // 개별 메트릭 조회
    private MetricData getMetric(String namespace, String metricName, String unit, 
                                Map<String, String> dimensions, String resourceType, String resourceName) {
        try {
            List<Dimension> dimensionList = dimensions.entrySet().stream()
                .map(entry -> Dimension.builder()
                    .name(entry.getKey())
                    .value(entry.getValue())
                    .build())
                .collect(Collectors.toList());
            
            GetMetricStatisticsRequest request = GetMetricStatisticsRequest.builder()
                .namespace(namespace)
                .metricName(metricName)
                .dimensions(dimensionList)
                .startTime(ZonedDateTime.now().minusMinutes(5).toInstant())
                .endTime(ZonedDateTime.now().toInstant())
                .period(300) // 5분
                .statistics(Statistic.AVERAGE)
                .build();
            
            GetMetricStatisticsResponse response = cloudWatchClient.getMetricStatistics(request);
            
            if (!response.datapoints().isEmpty()) {
                Datapoint datapoint = response.datapoints().get(0);
                return new MetricData(
                    resourceType,
                    resourceName,
                    metricName,
                    datapoint.average(),
                    unit,
                    LocalDateTime.ofInstant(datapoint.timestamp(), ZoneOffset.UTC)
                );
            }
            
        } catch (Exception e) {
            System.err.println("메트릭 조회 실패: " + namespace + "/" + metricName + " - " + e.getMessage());
        }
        
        return null;
    }
    
    // 대시보드용 최신 메트릭 데이터 조회
    public Map<String, List<MetricData>> getDashboardData() {
        Map<String, List<MetricData>> dashboardData = new HashMap<>();
        
        // EC2 메트릭
        dashboardData.put("EC2", metricDataRepository.findByResourceTypeAndResourceNameOrderByTimestampDesc("EC2", "tempEC2PUB"));
        
        // RDS 메트릭
        dashboardData.put("RDS", metricDataRepository.findByResourceTypeAndResourceNameOrderByTimestampDesc("RDS", "tempRDS"));
        
        // ALB 메트릭
        dashboardData.put("ALB", metricDataRepository.findByResourceTypeAndResourceNameOrderByTimestampDesc("ALB", "tempALB"));
        
        // Target Group 메트릭
        dashboardData.put("TG", metricDataRepository.findByResourceTypeAndResourceNameOrderByTimestampDesc("TG", "tempTG"));
        
        return dashboardData;
    }
    
    // 특정 리소스의 최신 메트릭 조회
    public List<MetricData> getLatestMetrics(String resourceType, String resourceName) {
        return metricDataRepository.findByResourceTypeAndResourceNameOrderByTimestampDesc(resourceType, resourceName);
    }
} 