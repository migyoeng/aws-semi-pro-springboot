package com.example.project.config;

import com.example.project.entity.Project;
import com.example.project.entity.Stack;
import com.example.project.repository.ProjectRepository;
import com.example.project.repository.StackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {
    
    @Autowired
    private ProjectRepository projectRepository;
    
    @Autowired
    private StackRepository stackRepository;
    
    @Override
    public void run(String... args) throws Exception {
        // 기존 데이터가 없을 때만 샘플 데이터 추가
        if (stackRepository.count() == 0) {
            createSampleStacks();
        }
        if (projectRepository.count() == 0) {
            createSampleProjects();
        }
    }
    
    private void createSampleStacks() {
        // 기술 스택들 생성
        Stack java = new Stack();
        java.setName("Java");
        java.setDescription("객체지향 프로그래밍 언어");
        
        Stack springBoot = new Stack();
        springBoot.setName("Spring Boot");
        springBoot.setDescription("Java 기반 웹 애플리케이션 프레임워크");
        
        Stack thymeleaf = new Stack();
        thymeleaf.setName("Thymeleaf");
        thymeleaf.setDescription("Java 서버 사이드 템플릿 엔진");
        
        Stack bootstrap = new Stack();
        bootstrap.setName("Bootstrap");
        bootstrap.setDescription("CSS 프레임워크");
        
        Stack jpa = new Stack();
        jpa.setName("JPA");
        jpa.setDescription("Java Persistence API");
        
        Stack aws = new Stack();
        aws.setName("AWS");
        aws.setDescription("Amazon Web Services 클라우드 플랫폼");
        
        Stack ec2 = new Stack();
        ec2.setName("EC2");
        ec2.setDescription("Amazon Elastic Compute Cloud");
        
        Stack rds = new Stack();
        rds.setName("RDS");
        rds.setDescription("Amazon Relational Database Service");
        
        Stack s3 = new Stack();
        s3.setName("S3");
        s3.setDescription("Amazon Simple Storage Service");
        
        Stack cloudFormation = new Stack();
        cloudFormation.setName("CloudFormation");
        cloudFormation.setDescription("AWS 인프라 자동화 서비스");
        
        Stack python = new Stack();
        python.setName("Python");
        python.setDescription("고급 프로그래밍 언어");
        
        Stack pandas = new Stack();
        pandas.setName("Pandas");
        pandas.setDescription("Python 데이터 분석 라이브러리");
        
        Stack matplotlib = new Stack();
        matplotlib.setName("Matplotlib");
        matplotlib.setDescription("Python 데이터 시각화 라이브러리");
        
        Stack seaborn = new Stack();
        seaborn.setName("Seaborn");
        seaborn.setDescription("Python 통계 데이터 시각화 라이브러리");
        
        Stack jupyter = new Stack();
        jupyter.setName("Jupyter");
        jupyter.setDescription("대화형 컴퓨팅 환경");
        
        Stack react = new Stack();
        react.setName("React");
        react.setDescription("JavaScript UI 라이브러리");
        
        Stack javascript = new Stack();
        javascript.setName("JavaScript");
        javascript.setDescription("웹 프로그래밍 언어");
        
        Stack nodejs = new Stack();
        nodejs.setName("Node.js");
        nodejs.setDescription("JavaScript 런타임 환경");
        
        Stack redux = new Stack();
        redux.setName("Redux");
        redux.setDescription("JavaScript 상태 관리 라이브러리");
        
        Stack mongodb = new Stack();
        mongodb.setName("MongoDB");
        mongodb.setDescription("NoSQL 데이터베이스");
        
        Stack springSecurity = new Stack();
        springSecurity.setName("Spring Security");
        springSecurity.setDescription("Spring 기반 보안 프레임워크");
        
        Stack jwt = new Stack();
        jwt.setName("JWT");
        jwt.setDescription("JSON Web Token");
        
        Stack mysql = new Stack();
        mysql.setName("MySQL");
        mysql.setDescription("관계형 데이터베이스");
        
        Stack docker = new Stack();
        docker.setName("Docker");
        docker.setDescription("컨테이너화 플랫폼");
        
        Stack dockerCompose = new Stack();
        dockerCompose.setName("Docker Compose");
        dockerCompose.setDescription("멀티 컨테이너 Docker 애플리케이션 관리");
        
        Stack linux = new Stack();
        linux.setName("Linux");
        linux.setDescription("오픈소스 운영체제");
        
        Stack cicd = new Stack();
        cicd.setName("CI/CD");
        cicd.setDescription("지속적 통합/지속적 배포");
        
        // 스택들 저장
        stackRepository.saveAll(Arrays.asList(
            java, springBoot, thymeleaf, bootstrap, jpa, aws, ec2, rds, s3, cloudFormation,
            python, pandas, matplotlib, seaborn, jupyter, react, javascript, nodejs, redux, mongodb,
            springSecurity, jwt, mysql, docker, dockerCompose, linux, cicd
        ));
        
        System.out.println("샘플 스택 데이터가 생성되었습니다.");
    }
    
    private void createSampleProjects() {
        // 스택들 조회
        Stack java = stackRepository.findByName("Java").orElse(null);
        Stack springBoot = stackRepository.findByName("Spring Boot").orElse(null);
        Stack thymeleaf = stackRepository.findByName("Thymeleaf").orElse(null);
        Stack bootstrap = stackRepository.findByName("Bootstrap").orElse(null);
        Stack jpa = stackRepository.findByName("JPA").orElse(null);
        Stack aws = stackRepository.findByName("AWS").orElse(null);
        Stack ec2 = stackRepository.findByName("EC2").orElse(null);
        Stack rds = stackRepository.findByName("RDS").orElse(null);
        Stack s3 = stackRepository.findByName("S3").orElse(null);
        Stack cloudFormation = stackRepository.findByName("CloudFormation").orElse(null);
        Stack python = stackRepository.findByName("Python").orElse(null);
        Stack pandas = stackRepository.findByName("Pandas").orElse(null);
        Stack matplotlib = stackRepository.findByName("Matplotlib").orElse(null);
        Stack seaborn = stackRepository.findByName("Seaborn").orElse(null);
        Stack jupyter = stackRepository.findByName("Jupyter").orElse(null);
        Stack react = stackRepository.findByName("React").orElse(null);
        Stack javascript = stackRepository.findByName("JavaScript").orElse(null);
        Stack nodejs = stackRepository.findByName("Node.js").orElse(null);
        Stack redux = stackRepository.findByName("Redux").orElse(null);
        Stack mongodb = stackRepository.findByName("MongoDB").orElse(null);
        Stack springSecurity = stackRepository.findByName("Spring Security").orElse(null);
        Stack jwt = stackRepository.findByName("JWT").orElse(null);
        Stack mysql = stackRepository.findByName("MySQL").orElse(null);
        Stack docker = stackRepository.findByName("Docker").orElse(null);
        Stack dockerCompose = stackRepository.findByName("Docker Compose").orElse(null);
        Stack linux = stackRepository.findByName("Linux").orElse(null);
        Stack cicd = stackRepository.findByName("CI/CD").orElse(null);
        
        // 프로젝트 1: 포트폴리오 웹사이트
        Project project1 = new Project();
        project1.setTitle("포트폴리오 웹사이트");
        project1.setDescription("Spring Boot와 Thymeleaf를 사용한 개인 포트폴리오 웹사이트입니다. 프로젝트 관리 기능과 반응형 디자인을 포함합니다.");
        project1.setProjectUrl("https://www.notion.so/portfolio-website-project");
        project1.setStacks(new HashSet<>(Arrays.asList(java, springBoot, thymeleaf, bootstrap, jpa)));
        
        // 프로젝트 2: AWS 클라우드 인프라
        Project project2 = new Project();
        project2.setTitle("AWS 클라우드 인프라");
        project2.setDescription("AWS 서비스를 활용한 클라우드 인프라 구축 프로젝트입니다. EC2, RDS, S3 등을 활용했습니다.");
        project2.setProjectUrl("https://www.notion.so/aws-cloud-infrastructure");
        project2.setStacks(new HashSet<>(Arrays.asList(aws, ec2, rds, s3, cloudFormation)));
        
        // 프로젝트 3: Python 데이터 분석
        Project project3 = new Project();
        project3.setTitle("Python 데이터 분석");
        project3.setDescription("Python을 사용한 데이터 분석 및 시각화 프로젝트입니다. pandas, matplotlib, seaborn을 활용했습니다.");
        project3.setProjectUrl("https://www.notion.so/python-data-analysis");
        project3.setStacks(new HashSet<>(Arrays.asList(python, pandas, matplotlib, seaborn, jupyter)));
        
        // 프로젝트 4: React 쇼핑몰
        Project project4 = new Project();
        project4.setTitle("React 쇼핑몰");
        project4.setDescription("React와 Node.js를 사용한 온라인 쇼핑몰 프로젝트입니다. Redux를 활용한 상태 관리와 결제 시스템을 구현했습니다.");
        project4.setProjectUrl("https://www.notion.so/react-shopping-mall");
        project4.setStacks(new HashSet<>(Arrays.asList(react, javascript, nodejs, redux, mongodb)));
        
        // 프로젝트 5: Spring Boot REST API
        Project project5 = new Project();
        project5.setTitle("Spring Boot REST API");
        project5.setDescription("Spring Boot를 사용한 RESTful API 서버입니다. JWT 인증, Swagger 문서화, MySQL 데이터베이스를 포함합니다.");
        project5.setProjectUrl("https://www.notion.so/spring-boot-rest-api");
        project5.setStacks(new HashSet<>(Arrays.asList(java, springBoot, springSecurity, jwt, mysql)));
        
        // 프로젝트 6: Docker 컨테이너화
        Project project6 = new Project();
        project6.setTitle("Docker 컨테이너화");
        project6.setDescription("Docker를 사용한 애플리케이션 컨테이너화 프로젝트입니다. Docker Compose를 활용한 멀티 컨테이너 환경을 구축했습니다.");
        project6.setProjectUrl("https://www.notion.so/docker-containerization");
        project6.setStacks(new HashSet<>(Arrays.asList(docker, dockerCompose, linux, cicd)));
        
        projectRepository.save(project1);
        projectRepository.save(project2);
        projectRepository.save(project3);
        projectRepository.save(project4);
        projectRepository.save(project5);
        projectRepository.save(project6);
        
        System.out.println("샘플 프로젝트 데이터가 생성되었습니다.");
    }
} 