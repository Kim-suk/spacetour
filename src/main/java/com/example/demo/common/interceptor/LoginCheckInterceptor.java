package com.example.demo.common.interceptor;

import java.net.URLEncoder;

import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class LoginCheckInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String uri = request.getRequestURI();

        // 예외 URI는 검사하지 않음
        if (uri.equals("/member/checkId") || uri.startsWith("/auth/") || uri.equals("/member/sendCode")) {
            return true;
        }

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("loginId") == null) {
            // AJAX 요청인지 확인
            String requestedWith = request.getHeader("X-Requested-With");
            if ("XMLHttpRequest".equals(requestedWith)) {
                // AJAX 요청 -> JSON 에러 응답
                response.setContentType("application/json;charset=UTF-8");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("{\"message\":\"게시글은 로그인 후 사용할 수 있습니다.\"}");
                return false;
            } else {
                // 일반 요청 -> 로그인 페이지로 리다이렉트
                String message = URLEncoder.encode("게시글은 로그인 후 사용할 수 있습니다.", "UTF-8");
                response.sendRedirect("/member/loginForm?message=" + message);
                return false;
            }
        }
        return true; // 로그인 상태이면 통과
    }
}
