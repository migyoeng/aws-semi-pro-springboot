package com.example.project.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "metric_data")
public class MetricData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "resource_type", nullable = false)
    private String resourceType; // EC2, RDS, ALB, TG

    @Column(name = "resource_name", nullable = false)
    private String resourceName; // tempEC2PUB, tempRDS, tempALB, tempTG

    @Column(name = "metric_name", nullable = false)
    private String metricName; // CPUUtilization, NetworkIn, NetworkOut, etc.

    @Column(name = "metric_value", nullable = false)
    private Double metricValue;

    @Column(name = "unit")
    private String unit; // Percent, Bytes, Count, etc.

    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;

    @Column(name = "region")
    private String region = "ap-northeast-2";

    // Constructors
    public MetricData() {}

    public MetricData(String resourceType, String resourceName, String metricName,
                      Double metricValue, String unit, LocalDateTime timestamp) {
        this.resourceType = resourceType;
        this.resourceName = resourceName;
        this.metricName = metricName;
        this.metricValue = metricValue;
        this.unit = unit;
        this.timestamp = timestamp;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public String getMetricName() {
        return metricName;
    }

    public void setMetricName(String metricName) {
        this.metricName = metricName;
    }

    public Double getMetricValue() {
        return metricValue;
    }

    public void setMetricValue(Double metricValue) {
        this.metricValue = metricValue;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }
}