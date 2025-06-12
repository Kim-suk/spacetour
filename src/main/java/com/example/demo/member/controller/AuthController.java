package com.example.demo.member.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.member.service.EmailAuthService;
import com.example.demo.member.service.MemberService;

@Controller
@RequestMapping("/auth")
public class AuthController {

	@Autowired
	MemberService service;
	
	@Autowired
	EmailAuthService emailAuthService;
 
  
     
    // 이메일로 아이디 찾기 (POST /auth/findIdByEmail)
    @PostMapping("/findIdByEmail")
    public ResponseEntity<Map<String, Object>> findIdByEmail(@RequestBody Map<String, String> payload) {
        String name = payload.get("name");
        String email = payload.get("email");

        Map<String, Object> response = new HashMap<>();
        if (name == null || email == null || name.isEmpty() || email.isEmpty()) {
            response.put("success", false);
            response.put("message", "이름과 이메일을 모두 입력해주세요.");
            return ResponseEntity.badRequest().body(response);
        }

        String id = emailAuthService.findIdByEmail(name, email);
        if (id != null) {
            // 아이디를 이메일로 전송
            emailAuthService.sendUserIdByEmail(email, id);

            response.put("success", true);
            response.put("message", "회원님의 아이디가 이메일로 전송되었습니다.");
        } else {
            response.put("success", false);
            response.put("message", "일치하는 회원 정보를 찾을 수 없습니다.");
        }

        return ResponseEntity.ok(response);
    } 


}

