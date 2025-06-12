package com.example.demo.userChat.dto;

import lombok.Data;
import java.util.List;

@Data
public class ChatRoomDetailDTO {
    private String roomId;
    private List<UserDTO> participants; // 참여자 목록

    public ChatRoomDetailDTO(String roomId, List<UserDTO> participants) {
        this.roomId = roomId;
        this.participants = participants;
    }

    public ChatRoomDetailDTO() {
    }
}
