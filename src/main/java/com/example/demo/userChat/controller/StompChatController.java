package com.example.demo.userChat.controller;

import com.example.demo.userChat.dto.ChatMessageDTO;
import com.example.demo.userChat.dto.MeMessageDTO;
import com.example.demo.userChat.service.ChatRoomLeaveService;
import com.example.demo.userChat.service.ChatService;
import com.example.demo.userChat.service.MeMessageService;

import lombok.RequiredArgsConstructor;

import java.util.Map;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/chat")
public class StompChatController {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatService chatService;
    private final MeMessageService meMessageService;
    private final ChatRoomLeaveService leaveService;

    @MessageMapping("/chat/send") // 클라이언트가 보낸 메시지 수신
    public void handleChatMessage(ChatMessageDTO message) {
    	 leaveService.recordLeave(message.getRoomId(), message.getSender());
        // 메시지 저장 (DB 또는 메모리)
    	  System.out.println("서버 수신 메시지: " + message);
        chatService.saveMessage(message);

        // 해당 채팅방 구독자에게 메시지 전송
        messagingTemplate.convertAndSend("/topic/chat/room/" + message.getRoomId(), message);
        
    }

    @MessageMapping("/chat/leave")
    public void handleLeave(ChatMessageDTO message) {
        message.setType(ChatMessageDTO.MessageType.LEAVE);
        message.setContent(message.getSender() + "님이 퇴장하셨습니다.");

        messagingTemplate.convertAndSend(
            "/topic/chat/room/" + message.getRoomId(), message);
    }

    @MessageMapping("/chat/delete")
    public void deleteMessage(ChatMessageDTO message) {
        chatService.deleteMessage(message.getId());  // DB 처리

        // 클라이언트에게 삭제된 메시지임을 알림
        message.setDeleted(true);

        // 구독 중인 사용자들에게 브로드캐스트
        messagingTemplate.convertAndSend("/topic/chat/room/" + message.getRoomId(), message);
    }

    
    @MessageMapping("/chat/delete/{roomId}")
    public void deleteMessage(@DestinationVariable String roomId, @Payload Map<String, Object> payload) {
        Long messageId = Long.valueOf(payload.get("messageId").toString());
        String userId = payload.get("userId").toString();

        try {
            ChatMessageDTO deletedMessage = chatService.deleteMessageAndNotify(messageId, userId);
            messagingTemplate.convertAndSend("/topic/chat/room/" + roomId, deletedMessage);
        } catch (RuntimeException e) {
            System.err.println("메시지 삭제 실패: " + e.getMessage());
        }
    }
    @MessageMapping("/chat.addUser")
    public void addUser(@Payload ChatMessageDTO chatMessage, SimpMessageHeaderAccessor headerAccessor) {
        // 세션에 사용자 이름과 방ID 저장
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
        headerAccessor.getSessionAttributes().put("roomId", chatMessage.getRoomId());

        chatMessage.setContent(chatMessage.getSender() + "님이 입장하셨습니다.");
        chatMessage.setType(ChatMessageDTO.MessageType.ENTER);

        // 특정 채팅방 구독자에게 입장 메시지 전송
        messagingTemplate.convertAndSend("/topic/chat/room/" + chatMessage.getRoomId(), chatMessage);
    }
    

    // 클라이언트가 /app/meRoom 으로 보내는 메시지를 처리
    @MessageMapping("/chat/chat") // 클라이언트가 보내는 메시지 경로
    public void processChatMessage(MeMessageDTO message) {
        System.out.println("받은 메시지: " + message);

        String receiver = message.getReceiver();
        if(receiver == null || receiver.isEmpty()) {
            System.err.println("receiver가 비어있습니다.");
            return;
        }

        // 메시지 저장 (필요시)
        meMessageService.saveToMyRoom(message);

        // receiver가 구독한 개인 채팅룸으로 메시지 발송
        messagingTemplate.convertAndSend("/topic/chat/meRoom/" + receiver, message);
    }
    
    @MessageMapping("/chat/forward")
    public void forwardMessage(Map<String, Object> payload) {
        String senderId = (String) payload.get("senderId");
        Long messageId = Long.valueOf(payload.get("messageId").toString());
        String forwardTo = (String) payload.get("forwardTo");

        boolean success = chatService.forwardMessage(senderId, messageId, forwardTo);
        if (success) {
            // 공유된 메시지 가져와서 구독자에게 전송 가능
            ChatMessageDTO forwardedMessage = chatService.getLatestForwardedMessage(senderId, forwardTo);
            messagingTemplate.convertAndSend("/topic/chat/room/" + forwardTo, forwardedMessage);
        } else {
            // 실패 시 별도 처리 가능 (에러 메시지 전송 등)
            System.err.println("메시지 전달 실패");
        }
    }



}

