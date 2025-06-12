package com.example.demo.userChat.controller;

import com.example.demo.userChat.dto.ChatMessageDTO;
import com.example.demo.userChat.service.ChatMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

//공지사항 서비스 (빵근이)
@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatAnnouncementController  {

    private final ChatMessageService chatMessageService;

    @PostMapping("/announce/{messageId}")
    public ResponseEntity<String> announceMessage(@PathVariable("messageId") Long messageId) {
    	  System.out.println("공지 설정 요청 messageId = " + messageId);
        chatMessageService.setAnnouncement(messageId);
        return ResponseEntity.ok("공지사항 설정 완료");
    }

    @GetMapping("/announcement/{roomId}")
    public ResponseEntity<ChatMessageDTO> getAnnouncement(@PathVariable("roomId") String roomId) {
        Optional<ChatMessageDTO> announcement = chatMessageService.getAnnouncement(roomId);
        return announcement.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.noContent().build());
    }
}
