// 대시보드 JavaScript
class Dashboard {
    constructor() {
        this.charts = {};
        this.updateInterval = null;
        this.isLoading = false;
        this.init();
    }

    init() {
        this.bindEvents();
        this.loadInitialData();
        this.startAutoRefresh();
    }

    bindEvents() {
        // 새로고침 버튼
        document.getElementById('refreshBtn').addEventListener('click', () => {
            this.refreshData();
        });

        // 메트릭 수집 버튼
        document.getElementById('collectBtn').addEventListener('click', () => {
            this.collectMetrics();
        });
    }

    async loadInitialData() {
        try {
            this.showLoading(true);
            const response = await fetch('/dashboard/api/refresh');
            if (response.ok) {
                const data = await response.json();
                this.updateDashboard(data);
                this.updateTimestamp();
            } else {
                this.showNotification('데이터 로드 실패', 'error');
            }
        } catch (error) {
            console.error('데이터 로드 오류:', error);
            this.showNotification('데이터 로드 중 오류가 발생했습니다', 'error');
        } finally {
            this.showLoading(false);
        }
    }

    async refreshData() {
        if (this.isLoading) return;
        
        try {
            this.showLoading(true);
            const response = await fetch('/dashboard/api/refresh');
            if (response.ok) {
                const data = await response.json();
                this.updateDashboard(data);
                this.updateTimestamp();
                this.showNotification('데이터가 새로고침되었습니다', 'success');
            } else {
                this.showNotification('새로고침 실패', 'error');
            }
        } catch (error) {
            console.error('새로고침 오류:', error);
            this.showNotification('새로고침 중 오류가 발생했습니다', 'error');
        } finally {
            this.showLoading(false);
        }
    }

    async collectMetrics() {
        if (this.isLoading) return;
        
        try {
            this.showLoading(true);
            const response = await fetch('/dashboard/collect-metrics', {
                method: 'POST'
            });
            
            if (response.ok) {
                this.showNotification('메트릭 수집이 완료되었습니다', 'success');
                // 수집 후 데이터 새로고침
                setTimeout(() => this.refreshData(), 1000);
            } else {
                const errorText = await response.text();
                this.showNotification('메트릭 수집 실패: ' + errorText, 'error');
            }
        } catch (error) {
            console.error('메트릭 수집 오류:', error);
            this.showNotification('메트릭 수집 중 오류가 발생했습니다', 'error');
        } finally {
            this.showLoading(false);
        }
    }

    updateDashboard(data) {
        // EC2 메트릭 업데이트
        this.updateEC2Metrics(data.EC2 || []);
        
        // RDS 메트릭 업데이트
        this.updateRDSMetrics(data.RDS || []);
        
        // ALB 메트릭 업데이트
        this.updateALBMetrics(data.ALB || []);
        
        // Target Group 메트릭 업데이트
        this.updateTGMetrics(data.TG || []);
    }

    updateEC2Metrics(metrics) {
        const cpuMetric = metrics.find(m => m.metricName === 'CPUUtilization');
        const networkInMetric = metrics.find(m => m.metricName === 'NetworkIn');
        const networkOutMetric = metrics.find(m => m.metricName === 'NetworkOut');

        if (cpuMetric) {
            document.getElementById('ec2Cpu').textContent = this.formatValue(cpuMetric.metricValue, 1);
        }
        if (networkInMetric) {
            document.getElementById('ec2NetworkIn').textContent = this.formatBytes(networkInMetric.metricValue);
        }
        if (networkOutMetric) {
            document.getElementById('ec2NetworkOut').textContent = this.formatBytes(networkOutMetric.metricValue);
        }

        this.updateChart('ec2Chart', 'EC2 메트릭', metrics);
    }

    updateRDSMetrics(metrics) {
        const cpuMetric = metrics.find(m => m.metricName === 'CPUUtilization');
        const connectionsMetric = metrics.find(m => m.metricName === 'DatabaseConnections');

        if (cpuMetric) {
            document.getElementById('rdsCpu').textContent = this.formatValue(cpuMetric.metricValue, 1);
        }
        if (connectionsMetric) {
            document.getElementById('rdsConnections').textContent = this.formatValue(connectionsMetric.metricValue, 0);
        }

        this.updateChart('rdsChart', 'RDS 메트릭', metrics);
    }

    updateALBMetrics(metrics) {
        const requestsMetric = metrics.find(m => m.metricName === 'RequestCount');

        if (requestsMetric) {
            document.getElementById('albRequests').textContent = this.formatValue(requestsMetric.metricValue, 0);
        }

        this.updateChart('albChart', 'ALB 메트릭', metrics);
    }

    updateTGMetrics(metrics) {
        const healthyHostsMetric = metrics.find(m => m.metricName === 'HealthyHostCount');
        const unhealthyHostsMetric = metrics.find(m => m.metricName === 'UnHealthyHostCount');
        const responseTimeMetric = metrics.find(m => m.metricName === 'TargetResponseTime');
        const requestsMetric = metrics.find(m => m.metricName === 'RequestCount');

        if (healthyHostsMetric) {
            document.getElementById('tgHealthyHosts').textContent = this.formatValue(healthyHostsMetric.metricValue, 0);
        }
        if (unhealthyHostsMetric) {
            document.getElementById('tgUnhealthyHosts').textContent = this.formatValue(unhealthyHostsMetric.metricValue, 0);
        }
        if (responseTimeMetric) {
            document.getElementById('tgResponseTime').textContent = this.formatValue(responseTimeMetric.metricValue, 3);
        }
        if (requestsMetric) {
            document.getElementById('tgRequests').textContent = this.formatValue(requestsMetric.metricValue, 0);
        }

        this.updateChart('tgChart', 'Target Group 메트릭', metrics);
    }

    updateChart(canvasId, title, metrics) {
        const canvas = document.getElementById(canvasId);
        if (!canvas) return;

        // 기존 차트 제거
        if (this.charts[canvasId]) {
            this.charts[canvasId].destroy();
        }

        const ctx = canvas.getContext('2d');
        
        // 차트 데이터 준비
        const labels = metrics.map(m => m.metricName);
        const values = metrics.map(m => m.metricValue || 0);
        const colors = this.getChartColors(metrics.length);

        this.charts[canvasId] = new Chart(ctx, {
            type: 'doughnut',
            data: {
                labels: labels,
                datasets: [{
                    data: values,
                    backgroundColor: colors,
                    borderWidth: 2,
                    borderColor: '#fff'
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                plugins: {
                    legend: {
                        position: 'bottom',
                        labels: {
                            padding: 20,
                            usePointStyle: true
                        }
                    },
                    title: {
                        display: true,
                        text: title,
                        font: {
                            size: 14,
                            weight: 'bold'
                        }
                    }
                },
                animation: {
                    animateRotate: true,
                    animateScale: true
                }
            }
        });
    }

    getChartColors(count) {
        const colors = [
            '#FF6384', '#36A2EB', '#FFCE56', '#4BC0C0',
            '#9966FF', '#FF9F40', '#FF6384', '#C9CBCF'
        ];
        return colors.slice(0, count);
    }

    formatValue(value, decimals = 2) {
        if (value === null || value === undefined) return '-';
        return Number(value).toFixed(decimals);
    }

    formatBytes(bytes) {
        if (bytes === null || bytes === undefined) return '-';
        
        const sizes = ['Bytes', 'KB', 'MB', 'GB', 'TB'];
        if (bytes === 0) return '0 Bytes';
        
        const i = Math.floor(Math.log(bytes) / Math.log(1024));
        return Math.round(bytes / Math.pow(1024, i) * 100) / 100 + ' ' + sizes[i];
    }

    updateTimestamp() {
        const now = new Date();
        const timeString = now.toLocaleString('ko-KR', {
            year: 'numeric',
            month: '2-digit',
            day: '2-digit',
            hour: '2-digit',
            minute: '2-digit',
            second: '2-digit'
        });
        document.getElementById('updateTime').textContent = timeString;
    }

    showLoading(show) {
        this.isLoading = show;
        const container = document.querySelector('.dashboard-container');
        
        if (show) {
            container.classList.add('loading');
        } else {
            container.classList.remove('loading');
        }
    }

    showNotification(message, type = 'info') {
        // 기존 알림 제거
        const existingNotification = document.querySelector('.notification');
        if (existingNotification) {
            existingNotification.remove();
        }

        // 새 알림 생성
        const notification = document.createElement('div');
        notification.className = `notification ${type}`;
        notification.textContent = message;
        
        document.body.appendChild(notification);
        
        // 애니메이션 표시
        setTimeout(() => {
            notification.classList.add('show');
        }, 100);
        
        // 3초 후 자동 제거
        setTimeout(() => {
            notification.classList.remove('show');
            setTimeout(() => {
                if (notification.parentNode) {
                    notification.remove();
                }
            }, 300);
        }, 3000);
    }

    startAutoRefresh() {
        // 5분마다 자동 새로고침
        this.updateInterval = setInterval(() => {
            this.refreshData();
        }, 5 * 60 * 1000);
    }

    stopAutoRefresh() {
        if (this.updateInterval) {
            clearInterval(this.updateInterval);
            this.updateInterval = null;
        }
    }
}

// 페이지 로드 시 대시보드 초기화
document.addEventListener('DOMContentLoaded', () => {
    new Dashboard();
});

// 페이지 언로드 시 정리
window.addEventListener('beforeunload', () => {
    if (window.dashboard) {
        window.dashboard.stopAutoRefresh();
    }
}); 