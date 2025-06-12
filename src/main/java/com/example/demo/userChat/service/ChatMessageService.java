package com.example.demo.userChat.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.userChat.dto.ChatMessageDTO;
import com.example.demo.userChat.repository.ChatMessageRepository;

import lombok.RequiredArgsConstructor;

// 공지사항 서비스 (빵근이)
@Service
@RequiredArgsConstructor
public class ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;

    @Transactional
    public void setAnnouncement(Long messageId) {
        ChatMessageDTO message = chatMessageRepository.findById(messageId)
                .orElseThrow(() -> new IllegalArgumentException("메시지를 찾을 수 없습니다."));

        // 기존 공지 해제
        chatMessageRepository.clearPreviousAnnouncement(message.getRoomId());

        // 공지 설정
        message.setAnnounced(true);
        chatMessageRepository.save(message);
    }

    public Optional<ChatMessageDTO> getAnnouncement(String roomId) {
        return chatMessageRepository.findFirstByRoomIdAndAnnouncedTrueOrderByTimestampDesc(roomId);
    }
}
