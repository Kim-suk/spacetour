package com.example.demo.userChat.handler;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

// TextWebSocketHandler를 상속했기 때문에 TextMessage를 처리하는 기능을 갖고 있음.
@Component
public class MyWebSocketHandler extends TextWebSocketHandler {
	// 클라이언트가 WebSocket을 통해 텍스트 메시지를 보내면 자동으로 이 메서드가 호출됩니다.
	// 클라이언트가 텍스트 메시지를 보낼 때마다 호출되는 콜백 메서드
	// session : 메시지를 보낸 클라이언트의 WebSocket 세션 객체
	// message : 클라이언트가 보낸 텍스트 메시지 객체
    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        
    	// WebSocket 연결 시 서버 측 세션 속성(attributes)에 저장해둔 "loginId" 값을 꺼냅니다.
    	// 보통 이 loginId는 HTTP 세션 → WebSocket 핸드셰이크 시점에 복사해둔 값입니다.
    	// 웹소켓 세션 attributes에서 loginId 꺼내기
        String loginId = (String) session.getAttributes().get("loginId");

        if (loginId == null) {
            session.sendMessage(new TextMessage("로그인이 필요합니다."));
            return;
        }
        
        // message.getPayload()는 사용자가 보낸 실제 텍스트(내용)를 문자열로 꺼내는 부분입니다.
        String chatMsg = message.getPayload();

        // 예: 채팅 메시지를 게시글로 저장하는 로직 호출 가능
        System.out.println(loginId + "님이 보낸 채팅 메시지: " + chatMsg);

        // 필요시 게시글 저장 서비스 호출 가능
        // boardService.save(new BoardDTO(...));

        session.sendMessage(new TextMessage("메시지 수신: " + chatMsg));
    }
}
