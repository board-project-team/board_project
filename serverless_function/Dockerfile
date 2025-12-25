# Cloud Run을 위한 기본 Python 이미지 사용
FROM python:3.10-slim

# 작업 디렉토리 설정
WORKDIR /app

# 의존성 파일 복사 및 설치
COPY requirements.txt .
RUN pip install --no-cache-dir -r requirements.txt

# 애플리케이션 파일 복사
COPY main.py .

# Cloud Run은 PORT 환경 변수를 사용합니다.
ENV PORT 8080
EXPOSE 8080

# Flask 앱 실행 명령어
CMD ["python", "main.py"]