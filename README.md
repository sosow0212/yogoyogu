<!--Header-->
# 1. Project
- <strong>Project name</strong> : 요고요구
- <strong>Develop Period</strong> : 2022.09 ~ 2022.10
- 배포 주소 : https://main--chimerical-malabi-ffde60.netlify.app/

---

# 2. Project Info
<hr>

- 명지대학교 ICT SW 개발 대회 프로젝트
- 학생회에게 건의하고 답변을 받을 수 있는 소통 커뮤니티입니다.

### 사용 기술

- React
- Spring boot
- JPA
- JUnit5 Test
- MySQL
- Docker, Docker-compose
- AWS (EC2, Load Balancer, Route 53)

### Project info
- Java11
- Gradle
- Jar
- Spring Boot 2.7.3



### Dockerfile
```dockerfile
FROM openjdk:11
ARG JAR_FILE=*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
```

### docker-compose.yml
```yml
version: '3.8'
services:
  mysqldb:
    image: mysql
    restart: always
    environment:
      MYSQL_DATABASE: yogoyogu
      MYSQL_ROOT_PASSWORD: root
    ports:
      - 3306:3306

  backend:
    build: .
    restart: always
    environment:
      SPRING_DATASOURCE_URL: jdbc:mysql://mysqldb:3306/yogoyogu
      SPRING_DATASOURCE_USERNAME: root
      SPRING_DATASOURCE_PASSWORD: root
    ports:
      - 8080:8080
    depends_on:
      - mysqldb
```

<hr>

### 서비스 피그마 (Frontend part)

회원가입

<img width="579" alt="image" src="https://user-images.githubusercontent.com/63213487/192086929-0af9da7a-92ad-4be7-b2c1-33eab1d2d994.png">

<br>

메일 인증

<img width="586" alt="image" src="https://user-images.githubusercontent.com/63213487/192086876-a6f93393-0b64-43be-9669-a956db9decef.png">

<br>

게시판 리스트

<img width="593" alt="image" src="https://user-images.githubusercontent.com/63213487/192086913-cea90d2c-2012-4844-b8b4-19f67fd2e162.png">

<br>

글 상세 페이지

<img width="836" alt="image" src="https://user-images.githubusercontent.com/63213487/192086946-9d86c00c-af8d-44c1-84f5-a7765f18e82c.png">

<br>


# 3. JUnit5 Test

Controller & Service unit test

<img width="650" alt="image" src="https://user-images.githubusercontent.com/63213487/192087138-26971e6c-c879-412a-9709-078f3e686dd0.png">

---

# 4. Convention
| **Git Convention** |
```text
Git Convention
feat : 기능추가
fix : 버그 수정
refactor : 리팩토링, 기능은 그대로 두고 코드를 수정
style : formatting, 세미콜론 추가 / 코드 변경은 없음
chore : 라이브러리 설치, 빌드 작업 업데이트
docs : 주석 추가 삭제, 문서 변경
```
