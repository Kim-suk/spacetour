package com.example.demo.userChat.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.userChat.dto.ChatMessageDTO;


public interface ChatMessageRepository extends JpaRepository<ChatMessageDTO, Long>{
	
	  // sender 기준으로 메시지 조회
    List<ChatMessageDTO> findBySender(String sender);

    // recipient 기준으로 메시지 조회
    List<ChatMessageDTO> findByRecipient(String recipient);

    // roomId + sender 조합으로 조회
    List<ChatMessageDTO> findByRoomIdAndSender(String roomId, String sender);

    // content 검색 포함
    List<ChatMessageDTO> findByContentContaining(String keyword);
    
    
    // 여기서부터 끝까지 빵근이가함(음하하하하)
    // 기존 공지 해제
    @Modifying
    @Query("UPDATE ChatMessageDTO m SET m.announced = false WHERE m.roomId = :roomId AND m.announced = true")
    void clearPreviousAnnouncement(@Param("roomId") String roomId);

    // 공지 메시지 1건 조회
    Optional<ChatMessageDTO> findFirstByRoomIdAndAnnouncedTrueOrderByTimestampDesc(String roomId);

    // 방 번호로 전체 메시지 조회
    List<ChatMessageDTO> findAllByRoomIdOrderByTimestampAsc(String roomId);

    // 특정 유저의 메시지
    List<ChatMessageDTO> findBySenderAndRoomIdOrderByTimestampDesc(String sender, String roomId);

    // 공지 메시지 전체 조회
    List<ChatMessageDTO> findAllByAnnouncedTrue();

    // 공지 여부 존재 확인
    boolean existsByRoomIdAndAnnouncedTrue(String roomId);
 
}
