package com.example.demo.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


import com.example.demo.common.interceptor.LoginCheckInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 업로드 폴더 매핑
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:" + System.getProperty("user.dir") + "/uploads/");
        registry.addResourceHandler("/uploaded/**")
                .addResourceLocations("file:" + System.getProperty("user.dir") + "/uploaded/");
        registry.addResourceHandler("/upload/profile/**")
                .addResourceLocations("file:///C:/springboot/workspace/GoldFlow/upload/profile/");
        
        // 정적 리소스 (classpath:/static/)
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:8080") // 허용할 프론트 주소
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowedHeaders("*")
                .allowCredentials(true); // 쿠키, 세션 공유 허용
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginCheckInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns(
                    "/", "/main/**",
                    "/member/**",
                    "/weather/**",
                    "/css/**", "/js/**", "/image/**","/auth/**",
                    "/uploads/**", "/uploaded/**"
                ); // 로그인 검사 제외 경로
    }
}
