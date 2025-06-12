package com.example.demo.userChat.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.userChat.dto.MeMessageDTO;
import com.example.demo.userChat.service.ChatService;
import com.example.demo.userChat.service.MeMessageService;

import jakarta.servlet.http.HttpSession;

// 채팅 페이지 반환 관련
@Controller
@RequestMapping("/chat")
public class ChatViewController {

	  @Autowired
	  ChatService chatService;
	    
	  @Autowired
	  MeMessageService meMessageService;
	    
    @GetMapping("/chat")
    public String chat(@RequestParam(value = "roomId", required = false) String roomId, Model model) {
        model.addAttribute("roomId", roomId);
        return "chat/chat";
    }

    @GetMapping("/room")
    public String chatRoomDetail(@RequestParam("roomId") String roomId, Model model) {
        model.addAttribute("roomId", roomId);
        return "chat/chat";
    }

    @GetMapping("/users")
    public String userList(@RequestParam("roomId") String roomId, Model model) {
        model.addAttribute("roomId", roomId);
        return "chat/users";
    }
  
    // "나와의 채팅" JSP 페이지 열기용
    @GetMapping("/meRoom")
    public String showMeRoom(Model model, HttpSession session) {
        String loginId = (String) session.getAttribute("loginId"); // 또는 로그인 처리된 사용자 ID
        model.addAttribute("loginId", loginId);
        return "chat/meRoom";
    }
    // 저장된 메시지 목록을 JSON으로 반환
    @GetMapping("/meRoom/messages")
    @ResponseBody
    public List<MeMessageDTO> getMessages(@RequestParam(name = "sender") String sender) {
	        return meMessageService.findMessagesBySender(sender);
    }



}
