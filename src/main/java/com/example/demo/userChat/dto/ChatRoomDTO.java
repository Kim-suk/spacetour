package com.example.demo.userChat.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class ChatRoomDTO {
    private String roomId;
    private String roomName;

    public ChatRoomDTO(String roomName) {
        this.roomId = UUID.randomUUID().toString(); // 고유 ID 생성
        this.roomName = roomName;
    }

	public ChatRoomDTO() {
		// TODO Auto-generated constructor stub
	}

	public void setName(String roomName2) {
		// TODO Auto-generated method stub
		
	}
}
