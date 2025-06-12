package com.example.demo.member.controller;

import java.util.Collections;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.member.repository.MemberRepository;

@RestController
public class MemberRestController {
		
	 @Autowired
	    private MemberRepository memberRepository;

    @RequestMapping("/member/checkLogin")
    public ResponseEntity<?> checkLogin() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken)) {
            return ResponseEntity.ok("인증된 사용자: " + auth.getName());
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 필요");
        }
    }
    
    // 이메일 중복 확인
    @GetMapping("/member/checkEmail")
    public Map<String, Boolean> checkEmail(@RequestParam("email") String email) {
        boolean exists = memberRepository.existsByEmail(email);
        return Collections.singletonMap("exists", exists);
    }

}
