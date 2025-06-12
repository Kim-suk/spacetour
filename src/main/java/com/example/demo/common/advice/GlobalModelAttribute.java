package com.example.demo.common.advice;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import jakarta.servlet.http.HttpSession;

@ControllerAdvice
public class GlobalModelAttribute {
   
   @ModelAttribute("loginId") // 전체적으로 세션 아이디값을 사용가능
   public String loginId(HttpSession session) {
      return (String) session.getAttribute("loginId");
            
   }
}