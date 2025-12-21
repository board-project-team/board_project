# 1단계: 빌드 스테이지 (Node.js 환경)
FROM node:18-alpine AS build
WORKDIR /app

# 의존성 설치 (캐시 활용을 위해 package.json만 먼저 복사)
COPY package*.json ./
RUN npm install

# 전체 소스 복사 및 빌드
COPY . .
# GKE 배포 시 백엔드 주소가 달라질 수 있으므로 환경변수 주입이 필요할 수 있습니다.
RUN npm run build

# 2단계: 실행 스테이지 (Nginx 환경)
FROM nginx:stable-alpine

# 빌드 스테이지에서 생성된 정적 파일(dist 폴더)을 Nginx의 기본 배포 경로로 복사
COPY --from=build /app/dist /usr/share/nginx/html

# Nginx 기본 포트 오픈
EXPOSE 80

# Nginx 서버 실행
CMD ["nginx", "-g", "daemon off;"]