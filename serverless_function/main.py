# Spring Boot 백엔드에서 호출되는 욕설 필터링 서버리스 API
from flask import Flask, request, jsonify
from flask_cors import CORS
import sys

app = Flask(__name__)
# 모든 도메인(origin)에서의 요청을 허용
CORS(app)

# 간단한 욕설/비속어 목록 (이 목록에 있는 단어가 감지되면 rejected 응답)
PROFANITY_LIST = [
    "시발", "병신", "개새끼", "좆", "씨발", "미친", "존나", "씨발", "fuck"
]

def check_profanity(content):
    """게시글 내용에 욕설이 포함되어 있는지 확인합니다."""
    # 내용을 소문자화하고 공백을 제거하여 비교합니다.
    normalized_content = content.lower().replace(" ", "")

    found_profanity = []
    for word in PROFANITY_LIST:
        if word in normalized_content:
            found_profanity.append(word)

    if found_profanity:
        return True, found_profanity
    return False, []

@app.route('/filter', methods=['POST'])
def filter_post():
    """게시글 내용을 받아 필터링 결과를 반환합니다."""
    print("LOG: [Filter 요청 수신] 분석을 시작합니다...", file=sys.stdout, flush=True)

    try:
        data = request.get_json()
        content = data.get('content', '')

        print(f"LOG: 분석 대상 내용: {content}", file=sys.stdout, flush=True)

        if not content:
            print("LOG: [Error] 내용이 비어있습니다.", file=sys.stderr, flush=True)
            return jsonify({"status": "error", "message": "Content field is missing"}), 400

        is_profane, words = check_profanity(content)

        if is_profane:
            # 욕설 감지 시 거부 응답
            print(f"LOG: [Detected] 감지된 단어: {words}", file=sys.stdout, flush=True)
            return jsonify({
                "status": "rejected",
                "message": "욕설이 감지되어 게시글 작성이 거부되었습니다.",
                "detected_words": words
            }), 200
        else:
            # 필터링 통과 시 승인 응답
            print("LOG: [Accepted] 욕설이 없습니다. 승인.", file=sys.stdout, flush=True)
            return jsonify({
                "status": "approved",
                "message": "게시글이 필터링을 통과했습니다."
            }), 200

    except Exception as e:
        # 서버 내부 오류 처리
        print(f"LOG: [Critical Error] 서버 내부 오류: {str(e)}", file=sys.stderr, flush=True)
        return jsonify({"status": "error", "message": f"Server error: {str(e)}"}), 500

if __name__ == '__main__':
    # Cloud Run은 PORT 환경 변수를 사용
    import os
    port = int(os.environ.get('PORT', 8080))
    app.run(host='0.0.0.0', port=port)