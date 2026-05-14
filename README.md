# School Predict Backend

학교 행사, 체육대회, 교내 e-sports 대회용 승부예측 백엔드입니다.

## 기술 스택

- Spring Boot
- Spring Security + JWT
- Spring Data JPA
- Redis
- Spring SSE
- MySQL
- Swagger

## 주요 흐름

1. 프론트가 `POST /auth/login`에 아이디와 비밀번호를 보냅니다.
2. 아이디가 없으면 같은 요청의 사용자 정보로 새 사용자를 생성합니다.
3. 아이디가 있으면 비밀번호를 검증하고 로그인합니다.
4. 서버는 자체 JWT를 발급합니다.
5. 이후 API는 `Authorization: Bearer <jwt>`로 호출합니다.

## 포인트와 배당률

- 사용자는 경기 시작 전 `PREDICTING` 상태인 경기만 예측할 수 있습니다.
- 예측 시 포인트가 즉시 차감됩니다.
- 배당률은 전체 베팅 풀과 선택 팀 베팅 풀을 기준으로 실시간 계산합니다.
- 예측이 저장될 때 해당 시점의 배당률이 고정됩니다.
- 결과 등록 시 `betPoint * odds`가 원금 포함 보상으로 지급됩니다.
- 기본 포인트는 첫 지갑 생성 시 1000점입니다.

## API

- `POST /auth/login`: 로그인 또는 최초 사용자 생성
- `GET /users/me`: 내 사용자 정보
- `GET /matches`: 진행 중/예정 경기 목록
- `POST /matches/{matchId}/predictions`: 예측 참여
- `GET /predictions/me`: 내 예측 목록
- `GET /points/me`: 내 포인트
- `GET /points/rankings`: 랭킹
- `GET /sse/odds`: 배당률 변경 SSE 구독
- `POST /admin/matches`: 경기 생성, ADMIN 전용
- `PUT /admin/matches/{matchId}`: 경기 수정, ADMIN 전용
- `DELETE /admin/matches/{matchId}`: 경기 삭제, ADMIN 전용
- `POST /admin/matches/{matchId}/result`: 결과 등록 및 정산, ADMIN 전용

## 환경변수

```bash
JWT_SECRET=temporary-one-off-secret-temporary-one-off-secret
JWT_EXPIRES_IN=5d
DB_URL=jdbc:mysql://localhost:3307/school_predict
DB_USERNAME=school_predict
DB_PASSWORD=school_predict
DB_DRIVER=com.mysql.cj.jdbc.Driver
REDIS_HOST=localhost
REDIS_PORT=6379
JPA_DDL_AUTO=update
```

## 실행

```bash
docker compose up -d
```

Swagger는 `/swagger-ui/index.html`에서 확인할 수 있습니다.
기본 Docker Compose 앱 포트는 `8081`입니다.

## CI/CD

`main` 브랜치에 push되면 GitHub Actions가 Docker 이미지를 빌드해서 Docker Hub에 올리고, 서버는 해당 이미지를 pull 받아 재시작합니다.

배포 흐름:

```text
main push
-> Docker image build
-> Docker Hub push
-> server docker compose pull
-> server docker compose up -d
```

GitHub Secrets:

- `DOCKER_USERNAME`: Docker Hub 사용자명
- `DOCKER_PASSWORD`: Docker Hub access token
- `SERVER_HOST`: 배포 서버 주소
- `SERVER_USER`: 배포 서버 SSH 사용자
- `SERVER_PASSWORD`: 배포 서버 SSH 비밀번호

GitHub Variables:

- `JWT_EXPIRES_IN`: `5d`
- `APP_PORT`: `8081`

서버의 기존 `~/school-predict/.env`에 있는 `JWT_SECRET`, `MYSQL_PASSWORD`, `MYSQL_ROOT_PASSWORD`는 배포 때 덮어쓰지 않습니다.

배포 후 Swagger:

```text
https://supdobby.me/swagger-ui/index.html
```
