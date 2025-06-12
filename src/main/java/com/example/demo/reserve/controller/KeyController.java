package com.example.demo.reserve.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class KeyController {
    @GetMapping("/api/config/toss-client-key")
    public String getTossClientKey() {
        return "test_ck_5OWRapdA8djbKLYomeRbVo1zEqZK"; // 실제 키로 대체
    }
}
