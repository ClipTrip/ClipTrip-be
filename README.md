# 🧳 ClipTrip-be

> 유튜브 여행 영상을 분석하여 개인화된 여행 일정을 추천하는 백엔드 API 서버

[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.5-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![MySQL](https://img.shields.io/badge/MySQL-8.0-blue.svg)](https://www.mysql.com/)
[![Redis](https://img.shields.io/badge/Redis-7.2.5-red.svg)](https://redis.io/)

## 📋 프로젝트 개요

**ClipTrip**은 유튜브 여행 영상의 URL을 입력받아 AI가 영상을 분석하고, 영상에 등장하는 여행지를 추출하여 사용자 맞춤형 여행 일정을 생성해주는 서비스입니다.

### ✨ 주요 특징
- 🎥 유튜브 영상 분석을 통한 여행지 자동 추출
- 🤖 AI 기반 여행 콘텐츠 분석 및 추천
- 📅 개인화된 여행 일정 생성
- 🔖 관심 장소 북마크 기능
- 🗺️ 여행 경로 최적화 및 길찾기
- 🌐 다국어 번역 지원

## 🛠️ 기술 스택

### Backend Framework
- **Java 21** 
- **Spring Boot 3.2.5** 
- **Spring Security** 
- **Spring Data JPA** 
- **Spring Validation** 

### Database & Cache
- **MySQL** 
- **Redis** 

### External APIs & Libraries
- **AWS S3** 
- **JWT** 
- **Swagger/OpenAPI** 
- **Jackson** 

### DevOps & Tools
- **Docker & Docker Compose** 
- **Gradle** 
- **GitHub Actions** 
- **Lombok** 

## 🎯 주요 기능

### 🎬 영상 분석
- 유튜브 URL 입력을 통한 영상 정보 추출
- AI 기반 영상 콘텐츠 분석
- 여행지 및 관광 명소 자동 식별

### 📍 장소 관리
- 추출된 여행지 정보 저장 및 관리
- 장소별 상세 정보 제공
- 사용자 맞춤 장소 추천

### 📅 일정 관리
- 개인화된 여행 일정 생성
- 일정 수정 및 공유 기능
- 최적화된 여행 경로 제안

### 🔖 북마크
- 관심 장소 북마크 저장
- 북마크 기반 일정 생성
- 개인 여행 위시리스트 관리

### 🔐 사용자 인증
- JWT 기반 토큰 인증
- 소셜 로그인 지원
- 사용자 프로필 관리

### 🗺️ 경로 및 길찾기
- 여행지 간 최적 경로 계산
- 교통수단별 이동 시간 제공
- 실시간 길찾기 정보

### 🌐 번역 서비스
- 다국어 여행 정보 번역
- 현지 언어 지원
- 실시간 번역 기능

## 📁 패키지 구조
```bash
src/main/java/com/cliptripbe/
├──  feature/                    # 도메인별 기능 모듈
│   ├──  video/                  # 영상 분석 기능
│   │   ├── api/                   # REST API 컨트롤러
│   │   ├── application/           # 애플리케이션 서비스
│   │   ├── domain/                # 도메인 로직
│   │   │   ├── entity/            # 도메인 엔티티
│   │   │   └── service/           # 도메인 서비스
│   │   ├── dto/                   # 데이터 전송 객체
│   │   │   ├── request/           # 요청 DTO
│   │   │   └── response/          # 응답 DTO
│   │   └── repository/            # 데이터 접근 계층
│   ├──  schedule/               # 일정 관리
│   ├──  bookmark/               # 북마크 기능
│   ├──  translate/              # 번역 서비스
│   ├──  auth/                   # 인증/인가
│   ├──  user/                   # 사용자 관리
│   ├──  direction/              # 길찾기
│   └──  place/                  # 장소 관리
├──  global/                     # 공통 기능
│   ├── auth/                      # 인증 공통 컴포넌트
│   │   ├── jwt/                   # JWT 처리
│   │   ├── security/              # Spring Security
│   │   └── cors/                  # CORS 설정
│   ├── config/                    # 설정 클래스
│   ├── constant/                  # 상수 정의
│   ├── entity/                    # 공통 엔티티
│   ├── exception/                 # 예외 처리
│   ├── initialization/            # 초기화 로직
│   ├── response/                  # 공통 응답 처리
│   └── util/                      # 유틸리티 클래스
└── 🔌 infrastructure/             # 인프라스트럭처
    ├── adapter/                   # 외부 시스템 연동
    └── port/                      # 포트 인터페이스
```
## ERD
![image](https://github.com/user-attachments/assets/ec739785-2c8b-41dc-83b0-9f909294ef38)

## System Architect
![image](https://github.com/user-attachments/assets/f6273f20-b9a5-4fa3-9f20-14be17c48f85)
