package com.example.demo.userChat.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ChatRoomSummaryDTO {
    private String roomId;
    private String roomName;
    private String lastMessage;
    private LocalDateTime lastMessageTime;
    private int unreadCount;

    public ChatRoomSummaryDTO(String roomId, String roomName, String lastMessage, LocalDateTime lastMessageTime, int unreadCount) {
        this.roomId = roomId;
        this.roomName = roomName;
        this.lastMessage = lastMessage;
        this.lastMessageTime = lastMessageTime;
        this.unreadCount = unreadCount;
    }

    public ChatRoomSummaryDTO() {
    }
}
