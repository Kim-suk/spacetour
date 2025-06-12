package com.example.demo.userChat.controller;

import java.security.Principal;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap.KeySetView;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.userChat.config.PageVisitUserTracker;
import com.example.demo.userChat.dto.ChatMessageDTO;
import com.example.demo.userChat.dto.MeMessageDTO;
import com.example.demo.userChat.service.ChatService;
import com.example.demo.userChat.service.MeMessageService;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/chat")
public class ChatRoomController {

	@Autowired
	ChatService chatService;
	
	 @Autowired
	 MeMessageService meMessageService;
	 
	 @Autowired
	 private PageVisitUserTracker pageVisitUserTracker;

	 
	// 1. 로그인 사용자의 채팅방 목록 조회
	@GetMapping("/rooms")
	public ResponseEntity<List<String>> getUserRooms(@RequestParam String user) {
		List<String> rooms = chatService.findRoomsByUser(user);
		if (rooms.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(rooms);
		}
		return ResponseEntity.ok(rooms);
	}

	// 2. 특정 채팅방의 메시지 전체 조회
	@GetMapping("/rooms/{roomId}/messages")
	public ResponseEntity<List<ChatMessageDTO>> getRoomMessages(@PathVariable String roomId) {
		List<ChatMessageDTO> messages = chatService.findMessagesByRoomId(roomId);
		if (messages.isEmpty()) {
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.ok(messages);
	}

	// 3. 본인과 관련된 채팅 메시지 목록 조회 (Principal 필요)
	@GetMapping("/messages")
	public ResponseEntity<List<ChatMessageDTO>> getMessages(@RequestParam String roomId, Principal principal) {

		if (principal == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}

		String userId = principal.getName();
		List<ChatMessageDTO> messages = chatService.getMessages(roomId, userId);
		if (messages.isEmpty()) {
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.ok(messages);
	}

	// 4. 채팅방 목록 (검색 포함)
	@GetMapping("/room/list")
	@ResponseBody
	public List<ChatMessageDTO> getUserChatRooms(@RequestParam("userId") String userId,
			@RequestParam(value = "keyword", required = false) String keyword) {
		return chatService.findRoomsByUser(userId, keyword);
	}

	// 5. 이전 메시지 페이지 단위로 조회
	@GetMapping("/{roomId}/history")
    public ResponseEntity<List<ChatMessageDTO>> getChatHistory(
            @PathVariable("roomId") String roomId,
            @RequestParam(name = "page") int page,
            @RequestParam(name = "size") int size) {
        List<ChatMessageDTO> messages = chatService.loadPreviousMessages(roomId, page, size);
        if (messages.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(messages);
    }
	
	@GetMapping("/connectedUsers")
    public ResponseEntity<?> getConnectedUsers() {
        try {
            Set<String> users = pageVisitUserTracker.getActiveUsers();
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류: " + e.getMessage());
        }
    }

    @PostMapping("/enter")
    public ResponseEntity<?> enterChatRoom(Principal principal) {
        try {
            if (principal != null) {
                pageVisitUserTracker.userEntered(principal.getName());
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("인증되지 않은 사용자입니다.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류: " + e.getMessage());
        }
    }
    @PostMapping("/leave")
    public ResponseEntity<?> leaveChatRoom(Principal principal) {
        try {
            if (principal != null) {
                pageVisitUserTracker.userLeft(principal.getName()); // 나간 사용자 처리
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("인증되지 않은 사용자입니다.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류: " + e.getMessage());
        }
    }

    public String generateRoomId(String userId1, String userId2) {
        List<String> users = Arrays.asList(userId1, userId2);
        Collections.sort(users);
        return users.get(0) + "_" + users.get(1);
    }

}
