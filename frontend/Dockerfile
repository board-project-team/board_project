# 1단계: 빌드 스테이지
FROM node:18-alpine AS build
WORKDIR /app

# ✅ [중요] 빌드 시점에 전달받을 변수 정의 (npm install 보다 위에 있어도 됨)
ARG VITE_API_BASE_URL
ENV VITE_API_BASE_URL=$VITE_API_BASE_URL

# 의존성 설치
COPY package*.json ./
RUN npm install

# 전체 소스 복사
COPY . .

# ✅ [핵심] 이 단계에서 ENV값이 .js 파일 내부로 영구히 박힙니다.
RUN npm run build

# 2단계: 실행 스테이지 (Nginx)
FROM nginx:stable-alpine

# ✅ [중요] SPA 라우팅을 위한 Nginx 설정 파일 복사 (파일이 같은 폴더에 있어야 함)
COPY default.conf /etc/nginx/conf.d/default.conf

# 빌드된 정적 파일을 Nginx 경로로 복사
COPY --from=build /app/dist /usr/share/nginx/html

EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
