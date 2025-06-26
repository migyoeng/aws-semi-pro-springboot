# AWS 인프라 모니터링 대시보드

Spring Boot, JPA, Thymeleaf를 활용한 AWS 인프라 모니터링 대시보드입니다.

## 🚀 주요 기능

### 📊 대시보드 위젯
- **EC2 인스턴스 모니터링**
  - CPU 사용률 (CPUUtilization)
  - 네트워크 인바운드 (NetworkIn)
  - 네트워크 아웃바운드 (NetworkOut)

- **RDS 데이터베이스 모니터링**
  - CPU 사용률 (CPUUtilization)
  - 데이터베이스 연결 수 (DatabaseConnections)

- **Application Load Balancer 모니터링**
  - 요청 수 (RequestCount)

- **Target Group 모니터링**
  - 정상 호스트 수 (HealthyHostCount)
  - 비정상 호스트 수 (UnHealthyHostCount)
  - 응답 시간 (TargetResponseTime)
  - 요청 수 (RequestCount)

### 🎯 추가 기능
- 실시간 메트릭 수집 및 표시
- Chart.js를 활용한 시각화
- 반응형 웹 디자인
- 자동 새로고침 (5분 간격)
- 수동 메트릭 수집 기능

## 🛠 기술 스택

- **Backend**: Spring Boot 3.5.3, JPA, Thymeleaf
- **Database**: MySQL, H2 (개발용)
- **Frontend**: HTML5, CSS3, JavaScript, Chart.js
- **AWS**: CloudWatch SDK, EC2, RDS, ALB
- **Build Tool**: Gradle

## 📋 요구사항

- Java 17+
- AWS 계정 및 자격 증명 설정
- MySQL 데이터베이스 (또는 H2)

## 🔧 설치 및 실행

### 1. 프로젝트 클론
```bash
git clone <repository-url>
cd project
```

### 2. AWS 자격 증명 설정
AWS CLI 또는 환경 변수를 통해 자격 증명을 설정하세요:

```bash
# AWS CLI 설정
aws configure

# 또는 환경 변수 설정
export AWS_ACCESS_KEY_ID=your_access_key
export AWS_SECRET_ACCESS_KEY=your_secret_key
export AWS_DEFAULT_REGION=ap-northeast-2
```

### 3. 데이터베이스 설정
`application.properties`에서 데이터베이스 연결 정보를 수정하세요:

```properties
spring.datasource.url=jdbc:mysql://your-rds-endpoint:3306/portfolio
spring.datasource.username=your_username
spring.datasource.password=your_password
```

### 4. 애플리케이션 실행
```bash
./gradlew bootRun
```

### 5. 접속
- 메인 페이지: http://localhost:8080
- 대시보드: http://localhost:8080/dashboard
- 관리자 페이지: http://localhost:8080/admin

## 📊 대시보드 사용법

### 메트릭 수집
1. **자동 수집**: 애플리케이션 시작 시 자동으로 샘플 데이터 생성
2. **수동 수집**: "메트릭 수집" 버튼 클릭
3. **실시간 업데이트**: "새로고침" 버튼 또는 5분마다 자동 업데이트

### 위젯 설명
- **EC2 위젯**: Auto Scaling Group의 인스턴스 성능 모니터링
- **RDS 위젯**: 데이터베이스 성능 및 연결 상태 모니터링
- **ALB 위젯**: 로드 밸런서 트래픽 모니터링
- **Target Group 위젯**: 백엔드 서버 상태 및 응답 시간 모니터링

## 🔍 API 엔드포인트

### 대시보드 API
- `GET /dashboard` - 대시보드 메인 페이지
- `GET /dashboard/api/refresh` - 메트릭 새로고침
- `POST /dashboard/collect-metrics` - 메트릭 수집
- `GET /dashboard/api/metrics/{resourceType}/{resourceName}` - 특정 리소스 메트릭 조회

## 📁 프로젝트 구조

```
src/main/java/com/example/project/
├── config/
│   └── DataInitializer.java          # 샘플 데이터 초기화
├── controller/
│   ├── DashboardController.java      # 대시보드 컨트롤러
│   ├── ProjectController.java        # 프로젝트 컨트롤러
│   └── WebController.java            # 웹 컨트롤러
├── entity/
│   ├── MetricData.java              # 메트릭 데이터 엔티티
│   ├── Project.java                 # 프로젝트 엔티티
│   └── Stack.java                   # 기술 스택 엔티티
├── repository/
│   ├── MetricDataRepository.java    # 메트릭 데이터 리포지토리
│   ├── ProjectRepository.java       # 프로젝트 리포지토리
│   └── StackRepository.java         # 스택 리포지토리
└── service/
    ├── MetricService.java           # 메트릭 서비스
    ├── ProjectService.java          # 프로젝트 서비스
    └── StackService.java            # 스택 서비스
```

## 🎨 UI/UX 특징

- **모던 디자인**: 그라데이션과 블러 효과를 활용한 현대적인 UI
- **반응형 레이아웃**: 모바일, 태블릿, 데스크톱 지원
- **인터랙티브 차트**: Chart.js를 활용한 동적 시각화
- **실시간 알림**: 작업 상태에 따른 토스트 알림
- **로딩 애니메이션**: 사용자 경험 향상을 위한 로딩 표시

## 🔒 보안 고려사항

- AWS IAM 역할을 통한 최소 권한 원칙 적용
- 환경 변수를 통한 민감 정보 관리
- HTTPS 통신 권장

## 🚨 주의사항

1. **AWS 비용**: CloudWatch API 호출 시 비용이 발생할 수 있습니다
2. **권한 설정**: EC2 인스턴스에 CloudWatch 읽기 권한이 필요합니다
3. **리전 설정**: `ap-northeast-2` 리전으로 설정되어 있습니다

## 📝 라이선스

이 프로젝트는 MIT 라이선스 하에 배포됩니다.

## 🤝 기여

버그 리포트, 기능 요청, 풀 리퀘스트를 환영합니다!

---

**개발자**: [Your Name]  
**버전**: 1.0.0  
**최종 업데이트**: 2025년 1월 