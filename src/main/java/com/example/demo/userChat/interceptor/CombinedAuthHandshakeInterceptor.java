package com.example.demo.userChat.interceptor;

import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

// CombinedAuthHandshakeInterceptor : WebSocket 연결(핸드셰이크) 시점에 클라이언트의 인증 정보(loginId) 를 확인하는 Interceptor 역할
// WebSocket 연결 전에 로그인 여부를 검사해서, 인증된 사용자만 WebSocket 연결을 허용하는 보안 필터 역할
public class CombinedAuthHandshakeInterceptor implements HandshakeInterceptor {
	
	
	// WebSocket 연결 전 검사
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                   WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {

        // HTTP 세션 가져오기
    	// WebSocket도 처음엔 HTTP 요청이므로, 이를 ServletServerHttpRequest로 변환 가능.
        if (request instanceof ServletServerHttpRequest servletRequest) {
        	// 기존 HTTP 세션이 존재하는지 확인. (false → 없으면 새로 만들지 않음)
            HttpSession httpSession = servletRequest.getServletRequest().getSession(false);

            if (httpSession != null) {
                // HTTP 세션에 loginId 있으면 바로 attributes에 넣기
            	// 기존 로그인된 사용자의 세션이라면 loginId를 WebSocket에 전달.
            	// 이후 WebSockt 핸들러 (MyWebSocketHandler)에서 session.getAttributes()로 꺼낼 수 있음
                String loginId = (String) httpSession.getAttribute("loginId");
                if (loginId != null) {
                    attributes.put("loginId", loginId);
                    attributes.put("HTTP_SESSION", httpSession);
                    return true;  // 인증 성공
                }
            }

            // HTTP 세션 없거나 loginId 없으면 Authorization 헤더에서 토큰 검사 (예시)
            // HttpHeaders : 객체를 통해 요청의 모든 HTTP 헤더를 가져옵니다.
            // Authorization : 헤더가 여기에 포함됩니다.
            HttpHeaders headers = request.getHeaders();
            // Authorization 헤더의 값을 꺼냅니다.
            // 예: "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6..."
            String authHeader = headers.getFirst("Authorization");
            
            // 헤더가 존재하고, "Bearer "로 시작하는지 확인합니다.
            // Bearer는 JWT나 OAuth 2.0 등의 인증 토큰 방식에서 사용되는 접두사입니다.
            // 예: "Bearer abc.def.ghi"
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
            	// "Bearer "는 총 7글자이므로, 8번째 문자부터 실제 토큰 부분만 추출합니다.
            	// 예: "Bearer abc.def.ghi" → "abc.def.ghi"
                String token = authHeader.substring(7);

                // 토큰 검증 로직 (예: JWT 검증 등) - 여기서는 단순히 "valid-token" 예시
                if (validateToken(token)) {
                    // 토큰이 유효하다면, 토큰에서 로그인 아이디 추출(예시)
                    String tokenLoginId = extractLoginIdFromToken(token);
                    if (tokenLoginId != null) {
                        attributes.put("loginId", tokenLoginId);
                        // HTTP_SESSION은 없으니 넣지 않음
                        return true;
                    }
                }
            }
        }

        // 인증 실패 시 연결 거부
        return false;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
                               WebSocketHandler wsHandler, Exception exception) {
        // 필요시 구현
    }

    // 토큰 유효성 검증 예시 (실제 로직에 맞게 구현하세요)
    private boolean validateToken(String token) {
        // TODO: JWT 등 실제 토큰 검증 로직 구현
        return "valid-token".equals(token);
    }

    // 토큰에서 loginId 추출 예시
    private String extractLoginIdFromToken(String token) {
        // TODO: JWT 파싱해서 로그인 아이디 추출
        if ("valid-token".equals(token)) {
            return "userFromToken";
        }
        return null;
    }
}
