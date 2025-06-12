package com.example.demo.userChat.controller;

import com.example.demo.userChat.dto.ChatMessageDTO;
import com.example.demo.userChat.dto.MeMessageDTO;
import com.example.demo.userChat.service.ChatService;
import com.example.demo.userChat.service.MeMessageService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatActionController {

    @Autowired
    private ChatService chatService;
    
    @Autowired
    MeMessageService meMessageService;

    private final SimpMessageSendingOperations messagingTemplate;

    @PostMapping("/forward/{loginId}")
    public ResponseEntity<String> forwardMessage(
            @PathVariable("loginId") String loginId,
            @RequestParam("messageId") Long messageId,
            @RequestParam("forwardTo") String forwardTo) {

        try {
            boolean success = chatService.forwardMessage(loginId, messageId, forwardTo);
            if (success) {
                return ResponseEntity.ok("메시지 전달 성공");
            } else {
                return ResponseEntity.badRequest().body("메시지 전달 실패");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("서버 에러로 메시지 전달 실패");
        }
    }
    
    // 메시지 전송
    @PostMapping("/sendMessage")
    public ResponseEntity<?> sendMessage(@RequestBody ChatMessageDTO message, HttpSession session) {
        String loginId = (String) session.getAttribute("loginId");
        if (loginId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("success", false, "message", "로그인이 필요합니다."));
        }
        message.setSender(loginId);
        message.setTimestamp(LocalDateTime.now());

        try {
            ChatMessageDTO saved = chatService.saveMessage(message);
            return ResponseEntity.ok(saved);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("success", false, "message", "메시지 저장 실패"));
        }
    }
    // 삭제
    @PostMapping("/delete/{msgId}")
    public ResponseEntity<Map<String, Object>> deleteMessage(@PathVariable("msgId") Long msgId, Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("success", false, "message", "로그인이 필요합니다."));
        }
        try {
            boolean deleted = chatService.deleteMessage(msgId, principal.getName());
            if (deleted) {
                // 삭제된 메시지 DTO 생성 (삭제 플래그 포함)
                ChatMessageDTO deletedMessage = new ChatMessageDTO();
                deletedMessage.setId(msgId);
                
                deletedMessage.setDeleted(true);

                // TODO: 삭제된 메시지의 roomId를 얻어야 함 (DB 조회 or 파라미터로 받기)
                Long roomId = chatService.getRoomIdByMessageId(msgId); // 예시 메서드

                // WebSocket 구독자들에게 삭제 알림 전송
                messagingTemplate.convertAndSend("/topic/chat/room/" + roomId, deletedMessage);

                return ResponseEntity.ok(Map.of("success", true, "message", "삭제 성공"));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("success", false, "message", "삭제할 메시지가 없습니다."));
            }
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(Map.of("success", false, "message", e.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("success", false, "message", "삭제 중 오류 발생"));
        }
    }
    @PostMapping("/meRoom")
    public ResponseEntity<?> sendToMyRoom(@RequestBody MeMessageDTO message) {
        message.setCreatedAt(LocalDateTime.now());

        MeMessageDTO savedMessage = chatService.saveToMyRoom(message);

        messagingTemplate.convertAndSend("/topic/chat/meRoom/" + message.getSender(), savedMessage);

        return ResponseEntity.ok("ok");
    }

}
