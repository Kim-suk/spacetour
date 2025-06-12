package com.example.demo.userChat.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import com.example.demo.userChat.handler.MyWebSocketHandler;
import com.example.demo.userChat.interceptor.CombinedAuthHandshakeInterceptor;

@Configuration   // 이 클래스는 Spring 설정 파일이라는 의미
@EnableWebSocketMessageBroker // STOMP를 사용하는 WebSocket 메시징을 활성화
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
// WebSocketMessageBrokerConfigurer : WebSocket 관련 설정 메서드를 오버라이드할 수 있게 해주는 인터페이스
    private final MyWebSocketHandler myWebSocketHandler;

    public WebSocketConfig(MyWebSocketHandler myWebSocketHandler) {
        this.myWebSocketHandler = myWebSocketHandler;
    }

    
    // registerStompEndpoints : 실시간 연결을 할 때 들어오는 문(출입구)를 만드는 역할
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws-chat") // 클라이언트가 WebSocket을 연결할 URL(ex: ws://localhost:8080/ws-chat)
        		// addInterceptors(...) : WebSocket 연결 시 인증/세션 등의 인터셉터를 추가
        		.addInterceptors(new CombinedAuthHandshakeInterceptor())  // 사용자 정의 인증 로직용 인터셉터(예:JWT 또는 세션 인증)
         		.addInterceptors(new HttpSessionHandshakeInterceptor()) // 기존 HttpSession의 정보를 WebSocekt 세션에 전달
                .setAllowedOriginPatterns("http://localhost:8080")  // CORS 허용 - 프론트가 다른 포트로 띄워진 경우 허용 필요
                .withSockJS(); // 브라우저 호환성을 위해 SockJS (WebSocket 미지원 환경 fallback)
    }
    
    // configureWebSocketTransport : 웹소캣으로 데이터를 주고받을 때 '운송(transpoart)'과 관련된 세부 설정
    // 한 번에 보낼 수 있는 메시지 최대 크기
    // 전송 가능한 버퍼 크기
    // 한 클라이언트가 보낼 수 있는 최대 메시지 수
    @Override
    public void configureWebSocketTransport(WebSocketTransportRegistration registration) {
        registration.setMessageSizeLimit(50 * 1024 * 1024); // 메시지 최대 크기 설정 (50MB)
        registration.setSendBufferSizeLimit(1024 * 1024); // 서버가 메시지를 버퍼링할 최대 크기
        registration.setSendTimeLimit(20 * 1000); // 한 메시지를 보내는 데 허용하는 최대 시간 (20초)
    }
    
    // configureMessageBroker : 서버와 클라이언트가 주고받는 메시지를 어떻게 중계(전달)할지 '중개자(브로커)'역할을 설정하는 곳
    // 누가 누구에게 메시지를 보내고 받는지, 메시지가 어떻게 흐를지 길을 정해주는 교통정리 담당자
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
    	// enableSimpleBroker(..) : 메시지를 구독 /전달할 브로커 경로 설정.
    	// /topic : 브로드캐스트(여러 명에게 메시지 전송)
    	// /queue : 1:1 큐 전송
        registry.enableSimpleBroker("/topic", "/queue");
        // setApplicationDestinationPrefixes("/app")
        // 클라이언트가 보내는 메시지가 /app/** 로 시작되면,
        // -> 해당 메시지를 서버의 @MessageMapping 메서드로 라우팅합니다.
        registry.setApplicationDestinationPrefixes("/app"); // 발행
    }

    // 필요하면 WebSocketHandler 직접 등록할 때 메서드 추가 가능
}
