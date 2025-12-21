# 1단계: 빌드 스테이지 (Node.js 환경)
FROM node:18-alpine AS build
WORKDIR /app

# 빌드 시점에 전달받을 변수 정의
ARG VITE_API_BASE_URL
ENV VITE_API_BASE_URL=$VITE_API_BASE_URL

# 의존성 설치 (캐시 활용을 위해 package.json만 먼저 복사)
COPY package*.json ./
RUN npm install

# 전체 소스 복사
COPY . .

# [수정] 빌드 실행
# 위에서 설정한 ENV값이 이 단계에서 정적 파일(.js) 내부로 주입됩니다.
RUN npm run build

# 2단계: 실행 스테이지 (Nginx 환경)
FROM nginx:stable-alpine

# 빌드 스테이지에서 생성된 정적 파일(dist 폴더)을 Nginx의 기본 배포 경로로 복사
COPY --from=build /app/dist /usr/share/nginx/html

# Nginx 기본 포트 오픈
EXPOSE 80

# Nginx 서버 실행
CMD ["nginx", "-g", "daemon off;"]