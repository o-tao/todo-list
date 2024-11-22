# Todo-list 프로젝트

## 📋 프로젝트 소개

이 프로젝트는 **백엔드 RESTful API** 기반으로 구현된 **Todo-list 관리 애플리케이션**입니다.   
사용자가 할 일을 생성, 수정, 검색할 수 있는 기능을 제공하며, Github Actions를 통한 **CI/CD 자동화**와 **AWS 인프라**를 활용한 배포 환경이 구축되어 있습니다.

## 📌 주요 기능

- ### 사용자 관리
    - 회원 가입 : 사용자 계정을 생성
    - 로그인, 로그아웃 : 세션 기반 인증 및 로그아웃 처리

- ### Todo 관리
    - Todo 생성 : 새로운 할 일 추가
    - Todo 조회 : Todo 리스트 조회 및 상세 내용 조회
    - Todo 검색 : 제목`title` 및 상태`status` 검색
    - Todo 수정 : 기존 할 일의 내용 수정
    - Todo 상태 변경: `TODO` ↔ `DONE` 상태 전환

- ### CI/CD 및 배포 자동화
    - GitHub Actions를 통한 CI/CD 파이프라인 구축
    - AWS 배포 자동화: EC2, RDS, ElastiCache, S3, CodeDeploy 활용

## 🛠️ 기술 스택

- **언어** : Java
- **프레임워크** : Spring Boot
- **데이터베이스**
    - MySQL
    - Redis
    - H2 (테스트 환경)
- **ORM** : Spring Data JPA, QueryDSL
- **배포 환경**
    - GitHub Actions: CI/CD 자동화
    - AWS
        - EC2 : 애플리케이션 서버
        - RDS : MySQL 데이터베이스
        - ElastiCache : Redis 캐시
        - S3 : 파일 저장
        - CodeDeploy : 배포 자동화
- **컨테이너화** : Docker Compose (개발 환경)

## 📚 API 명세서

API 명세서는 **Postman**을 통해 관리되며, 아래 링크에서 확인할 수 있습니다.

[API 명세서 링크](https://documenter.getpostman.com/view/38357139/2sAYBSktrc)

## 🚀 프로젝트 실행 방법

### 1. 프로젝트 클론

```bash
# 프로젝트 클론
$ git clone https://github.com/o-tao/todo-list.git

# 프로젝트 경로로 이동
$ cd todo-list
```

### 2. 기본 설정

application.yml 파일은 기본적으로 로컬 개발 환경에 맞춰 설정되어 있습니다. 별도의 배포 환경 설정은 필요하지 않으며, Spring Boot Profile로 환경별 설정을 관리합니다.

```yaml
spring:
  data:
    redis:
      host: localhost
      port: 6379

  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:23306/todo
    username: root
    password: 1234

  jpa:
    hibernate:
      ddl-auto: update
    properties:
      hibernate:
        show_sql: true
        format_sql: true
        dialect: org.hibernate.dialect.MySQL8Dialect

logging:
  level:
    org:
      hibernate:
        sql: debug
        type:
          descriptor:
            sql:
              BasicBinder: trace

server:
  port: 8081

```

### 3. Docker Compose로 데이터베이스 환경 구성

```bash
# -d : 백그라운드 실행
$ docker-compose up -d
```

### 4. 애플리케이션 실행

```bash
$ ./gradlew bootRun
```
