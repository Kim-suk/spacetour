package com.example.demo.common.security;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .csrf(csrf -> csrf.disable())
            // SecurityContext 설정 (명시적 등록)
            .securityContext(securityContext -> 
                securityContext.securityContextRepository(new HttpSessionSecurityContextRepository())
            )
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/reserve/reserve", "/main/mypage").authenticated()
                .anyRequest().permitAll()
            )
            .exceptionHandling(ex -> ex.authenticationEntryPoint((request, response, authException) -> {
                HttpSession session = request.getSession(false);
                System.out.println("Session loginId: " + (session != null ? session.getAttribute("loginId") : "null"));
                System.out.println("Auth: " + SecurityContextHolder.getContext().getAuthentication());
                boolean isApiRequest = "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));

                if (session == null || session.getAttribute("loginId") == null) {
                    response.setCharacterEncoding("UTF-8");
                    response.setContentType("application/json;charset=UTF-8");
                    response.getWriter().write("{\"success\":false, \"message\":\"로그인이 필요합니다.\"}");
                } else {
                    if (isApiRequest) {
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        response.getWriter().write("{\"success\":false, \"message\":\"인증되지 않았습니다.\"}");
                    } else {
                        response.sendRedirect("/member/loginForm");
                    }
                }
            }))
            .securityContext(securityContext -> securityContext
                    .securityContextRepository(new HttpSessionSecurityContextRepository()) // ★ 명시적 등록
                )
            .formLogin(form -> form.disable()) // 여기 추가: 수동 로그인 방식을 쓴다면 필수로 disable
            .logout(logout -> logout
                    .logoutUrl("/member/logout")
                    .logoutSuccessUrl("/member/loginForm")
                    .invalidateHttpSession(true)
                    .deleteCookies("JSESSIONID")
                );

            return http.build();
        }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:8080")); // CORS 허용 출처 (필요시 변경)
        configuration.setAllowCredentials(true);
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
        configuration.setAllowedHeaders(List.of("*"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public ServletContextInitializer servletContextInitializer() {
        return servletContext -> {
            SessionCookieConfig config = servletContext.getSessionCookieConfig();
            config.setSecure(false); // 개발 환경
            config.setHttpOnly(true);
            config.setName("JSESSIONID");
        };
    }

    @Bean
    public FilterRegistrationBean<SameSiteCookieFilter> sameSiteCookieFilter() {
        FilterRegistrationBean<SameSiteCookieFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new SameSiteCookieFilter(false)); // secure=false (개발)
        registrationBean.addUrlPatterns("/*");
        registrationBean.setOrder(Integer.MIN_VALUE);
        return registrationBean;
    }

    public static class SameSiteCookieFilter implements Filter {
        private final boolean secureFlag;

        public SameSiteCookieFilter(boolean secureFlag) {
            this.secureFlag = secureFlag;
        }

        @Override
        public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
                throws IOException, ServletException {
            HttpServletResponse res = (HttpServletResponse) response;
            CookieResponseWrapper responseWrapper = new CookieResponseWrapper(res, secureFlag);
            chain.doFilter(request, responseWrapper);
        }

        private static class CookieResponseWrapper extends HttpServletResponseWrapper {
            private final boolean secureFlag;

            public CookieResponseWrapper(HttpServletResponse response, boolean secureFlag) {
                super(response);
                this.secureFlag = secureFlag;
            }

            @Override
            public void addHeader(String name, String value) {
                if ("Set-Cookie".equalsIgnoreCase(name)) {
                    if (!value.toLowerCase().contains("samesite")) {
                        value += secureFlag ? "; SameSite=None" : "; SameSite=Lax";
                    }
                    if (secureFlag && !value.toLowerCase().contains("secure")) {
                        value += "; Secure";
                    }
                }
                super.addHeader(name, value);
            }
        }
    }
}

