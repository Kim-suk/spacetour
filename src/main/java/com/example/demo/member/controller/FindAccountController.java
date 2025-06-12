package com.example.demo.member.controller;

import com.example.demo.member.service.EmailAuthService;
import com.example.demo.member.service.PasswordResetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/member/find")
public class FindAccountController {

    @Autowired
    private EmailAuthService emailAuthService;

    @Autowired
    private PasswordResetService passwordResetService;

    

    // 아이디 찾기 (이메일 기반)
    @GetMapping("/find-id")
    public String findUserId(@RequestParam("email") String email) {
        return emailAuthService.findUserIdByEmail(email);
    }
}
	