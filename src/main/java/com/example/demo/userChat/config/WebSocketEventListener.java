package com.example.demo.userChat.config;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import com.example.demo.userChat.dto.ChatMessageDTO;


import ch.qos.logback.classic.Logger;

@Component
public class WebSocketEventListener {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(WebSocketEventListener.class);

    private final SimpMessagingTemplate messagingTemplate;
    private final SimpUserRegistry simpUserRegistry;

    @Autowired
    public WebSocketEventListener(SimpMessagingTemplate messagingTemplate, SimpUserRegistry simpUserRegistry) {
        this.messagingTemplate = messagingTemplate;
        this.simpUserRegistry = simpUserRegistry;
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

        // 세션에서 username 가져오기
        String username = (String) headerAccessor.getSessionAttributes().get("username");

        if (username != null) {
            logger.info("🔌 유저 퇴장 감지: {}", username);

            ChatMessageDTO leaveMessage = new ChatMessageDTO();
            leaveMessage.setType(ChatMessageDTO.MessageType.LEAVE);
            leaveMessage.setSender(username);
            leaveMessage.setContent(username + "님이 퇴장하셨습니다.");
            // 메시지를 /topic/public 으로 브로드캐스트
            messagingTemplate.convertAndSend("/topic/chat/room/" + getRoomIdFromSession(headerAccessor), leaveMessage);
        } else {
            logger.warn("세션에서 username을 찾을 수 없음: {}", headerAccessor.getSessionId());
        }
    }
    private String getRoomIdFromSession(StompHeaderAccessor headerAccessor) {
		// TODO Auto-generated method stub
		return null;
	}

}

