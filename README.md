# Auth-Service

**CSrrr 프로젝트**의 인증·인가(Auth) 마이크로서비스입니다.

## 주요 기능
- JWT 기반 회원가입, 로그인, 리프레시 토큰 처리
- Redis를 이용한 리프레시 토큰 검증 및 세션 관리
- AWS S3 연동을 통한 사용자 프로필 이미지 업로드
- Spring Security와 Feign Client를 활용한 API 보안 및 서비스 간 통신

## 기술 스택
- **언어 & 프레임워크**: Java 17, Spring Boot 3.5.0
- **보안 & 인증**: Spring Security, JWT (JJWT)
- **데이터베이스**: Spring Data JPA, MySQL
- **캐시 & 세션**: Spring Data Redis
- **API 통신**: OpenFeign
- **편의 라이브러리**: Lombok, HikariCP
- **스토리지**: AWS S3
- **인프라 & 배포**: Docker, ArgoCD 