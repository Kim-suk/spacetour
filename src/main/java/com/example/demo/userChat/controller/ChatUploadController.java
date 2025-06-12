package com.example.demo.userChat.controller;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
// 이미지 업로드 전용
@RestController
@RequestMapping("/chat")
public class ChatUploadController {

	@PostMapping("/uploadImage")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> uploadImage(@RequestParam("file") MultipartFile file) {
        Map<String, Object> result = new HashMap<>();

        // 업로드 경로 (프로젝트 실행 경로 기준)
        String uploadDir = System.getProperty("user.dir") + "/uploaded/chat/";

        // 디렉토리 없으면 생성
        File dir = new File(uploadDir);
        if (!dir.exists() && !dir.mkdirs()) {
            result.put("status", "fail");
            result.put("error", "업로드 디렉토리 생성 실패");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
        }

        // 파일명 고유화
        String originalFilename = file.getOriginalFilename();
        String fileName = UUID.randomUUID() + "_" + (originalFilename != null ? originalFilename : "unknown");

        File destination = new File(uploadDir + fileName);
        try {
            file.transferTo(destination);
            result.put("status", "success");
            // 클라이언트가 사용할 URL 경로, 반드시 정적 리소스 매핑 필요!
            result.put("imageUrl", "/uploaded/chat/" + fileName);
            return ResponseEntity.ok(result);
        } catch (IOException e) {
            e.printStackTrace();
            result.put("status", "fail");
            result.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
        }
    }
}
