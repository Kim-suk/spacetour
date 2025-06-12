package com.example.demo.userChat.service;

import java.util.List;

import com.example.demo.userChat.dto.MeMessageDTO;

public interface MeMessageService {
	void saveMessage(MeMessageDTO dto);
	List<MeMessageDTO> getMessages(String sender);

	void saveToMyRoom(MeMessageDTO message);

	List<MeMessageDTO> findMessagesBySender(String sender);
}
