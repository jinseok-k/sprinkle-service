## 카카오페이 뿌리기 기능 구현하기

### 개발 환경
- 개발언어: Java (jdk1.8.0_231)
- DB: MariaDB (10.3.24)

### DB 스키마는 아래 파일 참고
resources/db.migration/V1.0__init_database.sql

### 에러코드는 아래 파일 참고
com.example.sprinkle.common.code.ErrorCode

### 문제접근 및 제약사항
- token 3자리 문자열로 제한됨에 따라 모든 API 는 대화방에서 이루어진다고 가정
- 대화방 아이디와 token 조합으로 토큰을 관리하며, 각 대화방 내 뿌리기 동시 진행 건은 대략 1000개 정도로 제한
- 조회 가능 기간(7일)이 지난 토큰 정보는 삭제(스케줄링 서비스로 생략)
- 뿌리기 인원수는 최소 1명
- 뿌리는 금액의 최소값은 인원수이며, 최대는 1천만원으로 한정
- 뿌리기에 대한 줍기가 끝난 경우 유효시간(10분) 상관없이 마감 응답
- 토큰관리와 줍기 테이블은 선점하는 방식으로 처리(저장 시 중복이면 재시도)

### 실행 방법
1. mariadb docker 실행
docker run -d --name test-mariadb --net shared -p 3306:3306 -e MYSQL_ROOT_PASSWORD=password -e MYSQL_USER=username -e MYSQL_PASSWORD=password -e MYSQL_DATABASE=users -v test-mariadb:/var/lib/maria mariadb:10.3.24 --character-set-server=utf8 --collation-server=utf8_general_ci

2. mariadb 접속 후 sprinkle 스키마 생성

3. sprinkle-service maven compile 실행

4. sprinkle-service Application 실행
