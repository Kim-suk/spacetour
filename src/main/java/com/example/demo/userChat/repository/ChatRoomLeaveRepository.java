package com.example.demo.userChat.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.userChat.dto.ChatMessageDTO;
import com.example.demo.userChat.dto.ChatRoomLeave;

import jakarta.transaction.Transactional;

@Repository
public interface ChatRoomLeaveRepository extends JpaRepository<ChatRoomLeave, Long> {
    
    // 특정 유저가 나간 방 리스트 조회 예시 (필요시)
    List<ChatRoomLeave> findByUserId(String userId);

    // 특정 유저가 나간 특정 방 존재 여부 확인 (필요시)
    boolean existsByRoomIdAndUserId(String roomId, String userId);
}