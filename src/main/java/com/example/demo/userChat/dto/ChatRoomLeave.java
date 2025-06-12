package com.example.demo.userChat.dto;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Entity
@Table(name = "chat_room_leave")
public class ChatRoomLeave {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "room_id", nullable = false)
    private String roomId;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "leave_time", nullable = false)
    private LocalDateTime leaveTime = LocalDateTime.now();

    // 기본 생성자
    public ChatRoomLeave() {}

    // 생성자 편의 메서드
    public ChatRoomLeave(String roomId, String userId) {
        this.roomId = roomId;
        this.userId = userId;
        this.leaveTime = LocalDateTime.now();
    }

    // getter, setter 생략 (필요시 lombok @Getter, @Setter 사용 가능)
    public Long getId() { return id; }
    public String getRoomId() { return roomId; }
    public void setRoomId(String roomId) { this.roomId = roomId; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public LocalDateTime getLeaveTime() { return leaveTime; }
    public void setLeaveTime(LocalDateTime leaveTime) { this.leaveTime = leaveTime; }
}