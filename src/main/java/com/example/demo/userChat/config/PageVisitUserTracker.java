package com.example.demo.userChat.config;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentHashMap.KeySetView;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import com.example.demo.userChat.handler.MyWebSocketHandler;
import com.example.demo.userChat.interceptor.CombinedAuthHandshakeInterceptor;

@Component
public class PageVisitUserTracker {

    // 접속자명과 마지막 접속 시간(밀리초)
    private final ConcurrentHashMap<String, Long> activeUsers = new ConcurrentHashMap<>();

    // 페이지 접속 시 호출
    public void userEntered(String username) {
        activeUsers.put(username, System.currentTimeMillis());
    }

    // 주기적으로 오래된 접속자 제거 (예: 1분 이상 접속 신호 없으면 제거)
    public void removeInactiveUsers() {
        long cutoff = System.currentTimeMillis() - (60 * 1000); // 1분 전
        activeUsers.entrySet().removeIf(entry -> entry.getValue() < cutoff);
    }

    public KeySetView<String, Long> getActiveUsers() {
        removeInactiveUsers();
        return activeUsers.keySet();
    }

    public void userLeft(String username) {
        activeUsers.remove(username);
    }

}
