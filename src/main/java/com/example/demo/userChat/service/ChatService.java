package com.example.demo.userChat.service;

import com.example.demo.userChat.dto.ChatMessageDTO;
import com.example.demo.userChat.dto.MeMessageDTO;

import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
@Service
@Primary
public interface ChatService {

    ChatMessageDTO saveMessage(ChatMessageDTO message);

    List<ChatMessageDTO> getMessagesBetween(String sender, String recipient);

    List<ChatMessageDTO> getAllMessagesInRoom(String roomId);

    List<ChatMessageDTO> getVisibleMessagesForUser(String roomId, String userId);

    List<ChatMessageDTO> loadPreviousMessages(String roomId, int page, int size);

    Map<String, Long> getUnreadCounts(String userId);

    List<String> findRoomsByUserId(String userId);

    List<ChatMessageDTO> getUserRoomsSortedByLatestMessage(String userId);

    boolean forwardMessage(String userId, String forwardTo, Long messageId);

    List<ChatMessageDTO> findRoomsByUser(String userId, String keyword);

    List<ChatMessageDTO> findMessagesByRoomId(String roomId);

    List<ChatMessageDTO> getMessages(String roomId, String name);

    boolean isUserInRoom(String name, String roomId);

    List<ChatMessageDTO> findRoomsByUserIdAndKeyword(String userId, String keyword);


	List<String> findRoomsByUser(String user);

	List<ChatMessageDTO> findRoomsByIdsAndKeyword(List<String> activeRoomIds, String keyword);

	  boolean markMessageAsRead(Long messageId, String userId);

	void markMessagesAsRead(String roomId, String userId);

	int getUnreadCount(String userId);

	int getUnreadCountByRoom(String roomId, String userId);

	 boolean deleteMessage(Long messageId);
	    ChatMessageDTO deleteMessageAndNotify(Long messageId, String username);

		boolean deleteMessage(Long msgId, String name);

		Long getRoomIdByMessageId(Long msgId);

		void save(ChatMessageDTO msg);

		List<ChatMessageDTO> load(String sender);

		List<MeMessageDTO> findMessagesBySender(String sender);

		MeMessageDTO saveToMyRoom(MeMessageDTO message);
		boolean forwardMessage(String loginId, Long messageId, String forwardTo);

		ChatMessageDTO getLatestForwardedMessage(String senderId, String forwardTo);
		  // 두 사용자 ID로 방 ID 생성
	    String generateRoomId(String userId1, String userId2);

	    // 방 ID 기반으로 메시지 리스트 조회
	    List<ChatMessageDTO> getMessagesByRoomId(String userId1, String userId2);



		

}
