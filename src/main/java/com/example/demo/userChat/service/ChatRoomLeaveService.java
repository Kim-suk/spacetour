package com.example.demo.userChat.service;

import com.example.demo.userChat.dto.ChatRoomLeave;
import com.example.demo.userChat.repository.ChatRoomLeaveRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class ChatRoomLeaveService {

    private final ChatRoomLeaveRepository leaveRepository;

    public ChatRoomLeaveService(ChatRoomLeaveRepository leaveRepository) {
        this.leaveRepository = leaveRepository;
    }

    @Transactional
    public void recordLeave(String roomId, String userId) {
        // 중복 방지용 로직 필요시 추가 가능
        if (!leaveRepository.existsByRoomIdAndUserId(roomId, userId)) {
            ChatRoomLeave leave = new ChatRoomLeave(roomId, userId);
            leaveRepository.save(leave);
        }
    }
}
