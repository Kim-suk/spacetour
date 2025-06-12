package com.example.demo.userChat.service;

import com.example.demo.userChat.dto.ChatMessageDTO;
import com.example.demo.userChat.dto.MeMessageDTO;
import com.example.demo.userChat.repository.ChatRepository;
import com.example.demo.userChat.repository.MeMessageRepository;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class ChatServiceImpl implements ChatService {

     ChatRepository chatRepository;
    MeMessageRepository meMessageRepository;
    SimpMessageSendingOperations messagingTemplate;

    private static final long MESSAGE_DELETE_LIMIT_MINUTES = 5;

    @Autowired
    public ChatServiceImpl(ChatRepository chatRepository,
                          
                           SimpMessageSendingOperations messagingTemplate,
                           MeMessageRepository meMessageRepository) {
        this.chatRepository = chatRepository;
        
        this.messagingTemplate=messagingTemplate;
        this.meMessageRepository = meMessageRepository;
    }

    @Override
    @Transactional
    public ChatMessageDTO saveMessage(ChatMessageDTO message) {
        try {
            System.out.println("저장 전 메시지: " + message);
            ChatMessageDTO saved = chatRepository.save(message);
            System.out.println("저장 완료 메시지 ID: " + saved.getId());
            return saved;
        } catch (Exception e) {
            System.err.println("메시지 저장 실패: " + e.getMessage());
            throw new RuntimeException("메시지 저장 중 오류 발생", e);
        }
    }

    private boolean canDelete(ChatMessageDTO message) {
        LocalDateTime sentTime = message.getTimestamp();
        if (sentTime == null) return false;

        Duration duration = Duration.between(sentTime, LocalDateTime.now());
        return duration.toMinutes() <= MESSAGE_DELETE_LIMIT_MINUTES;
    }

    @Override
    @Transactional
    public List<ChatMessageDTO> getMessagesBetween(String sender, String recipient) {
        return chatRepository.findBySenderAndRecipientOrRecipientAndSenderOrderByTimestampAsc(sender, recipient, sender, recipient);
    }

    @Override
    @Transactional
    public List<ChatMessageDTO> getAllMessagesInRoom(String roomId) {
        return chatRepository.findByRoomIdOrderByTimestampAsc(roomId);
    }

    @Override
    @Transactional
    public List<ChatMessageDTO> getVisibleMessagesForUser(String roomId, String userId) {
        return chatRepository.findAllByRoomIdAndUserNotDeleted(roomId, userId);
    }

    @Override
    @Transactional
    public List<ChatMessageDTO> loadPreviousMessages(String roomId, int page, int size) {
        PageRequest pageable = PageRequest.of(page, size);
        List<ChatMessageDTO> messages = chatRepository.findPreviousMessagesByRoomId(roomId, pageable);
        Collections.reverse(messages);
        return messages;
    }

    @Override
    @Transactional
    public Map<String, Long> getUnreadCounts(String userId) {
        List<Object[]> results = chatRepository.countUnreadMessagesByUser(userId);
        Map<String, Long> counts = new HashMap<>();
        for (Object[] row : results) {
            String roomId = (String) row[0];
            Long count = (Long) row[1];
            counts.put(roomId, count);
        }
        return counts;
    }

    // 메시지 읽음 처리 (읽은 유저 추가 및 unreadCount 0 처리)
    @Transactional
    public void markMessagesAsRead(String roomId, String readerUserId) {
        List<ChatMessageDTO> messages = chatRepository.findByRoomIdOrderByTimestampAsc(roomId);

        for (ChatMessageDTO msg : messages) {
            // 내가 보낸 메시지이고, 상대가 아직 읽지 않은 메시지만 처리
            if (msg.getSender().equals(readerUserId)) {
                // 본인 메시지 -> 읽음 여부 변경 안함 (내가 읽은 건 당연)
                continue;
            }
            if (!msg.isReadByUser(readerUserId)) {
                msg.markAsReadBy(readerUserId);
                chatRepository.save(msg);
            }
        }
    }

    @Override
    @Transactional
    public List<String> findRoomsByUserId(String userId) {
        return chatRepository.findDistinctRoomsByUser(userId);
    }

    @Override
    @Transactional
    public List<ChatMessageDTO> getUserRoomsSortedByLatestMessage(String userId) {
        return chatRepository.findUserRoomsOrderByLatestMessage(userId);
    }

    @Override
    @Transactional
    public List<ChatMessageDTO> findRoomsByUser(String userId, String keyword) {
        return chatRepository.findRoomsByUserAndKeyword(userId, keyword);
    }

    @Override
    @Transactional
    public List<ChatMessageDTO> findMessagesByRoomId(String roomId) {
        return chatRepository.findByRoomIdOrderByTimestampAsc(roomId);
    }

    public boolean forwardMessage(String sender, String forwardTo, Long messageId) {
        Optional<ChatMessageDTO> originalMessageOpt = chatRepository.findById(messageId);
        if (!originalMessageOpt.isPresent()) {
            return false; // 원본 메시지 없으면 실패 처리
        }

        ChatMessageDTO originalMessage = originalMessageOpt.get();

        ChatMessageDTO newMessage = new ChatMessageDTO();
        newMessage.setSender(sender);
        newMessage.setRecipient(forwardTo);  // receiver가 아니라 recipient
        newMessage.setContent(originalMessage.getContent());
        newMessage.setRoomId(originalMessage.getRoomId());  // roomId도 복사

        // type은 enum이므로 null이면 TEXT 타입으로 설정
        newMessage.setType(originalMessage.getType() != null ? originalMessage.getType() : ChatMessageDTO.MessageType.TEXT);

        newMessage.setTimestamp(LocalDateTime.now()); // 현재 시간으로 설정
        newMessage.setUnreadCount(1);
        newMessage.setRead(false);
        newMessage.setDeletedBySender(false);
        newMessage.setDeletedByRecipient(false);
        newMessage.setAnnounced(false);

        // 이미지 메시지라면 imageUrl도 복사
        if (originalMessage.isImageMessage()) {
            newMessage.setImageUrl(originalMessage.getImageUrl());
        }

        chatRepository.save(newMessage);
        return true;
    }



    @Override
    @Transactional
    public boolean isUserInRoom(String name, String roomId) {
        // 예시: 채팅 메시지 저장소에 해당 룸(roomId)에서 name이 sender 혹은 recipient로 존재하는지 확인
        List<ChatMessageDTO> messages = chatRepository.findByRoomIdAndSenderOrRecipient(roomId, name, name);
        return !messages.isEmpty();
    }


@Override
@Transactional
public List<ChatMessageDTO> findRoomsByUserIdAndKeyword(String userId, String keyword) {
    // keyword로 룸ID나 메시지 내용 등을 검색하는 로직
    // 채팅 메시지 내용에 keyword가 포함된 메시지가 있는 룸을 반환
    return chatRepository.findRoomsByUserAndKeyword(userId, keyword);
}

	@Override
	public List<String> findRoomsByUser(String user) {
		// TODO Auto-generated method stub
		return null;
	}
	 /**
     * 특정 메시지를 특정 사용자가 읽음 처리
	 * @return 
     */
	@Transactional
	public boolean markMessageAsRead(Long messageId, String userId) {
	    ChatMessageDTO message = chatRepository.findById(messageId)
	        .orElseThrow(() -> new IllegalArgumentException("Message not found with id: " + messageId));

	    if (message.getReadByUsers().contains(userId)) {
	        return false;
	    }

	    message.getReadByUsers().add(userId);

	    // unreadCount 계산 예시 (전체 참여자 - 읽은 사용자)
	    int unreadCount = message.getTotalRecipients() - message.getReadByUsers().size();
	    message.setUnreadCount(Math.max(unreadCount, 0));

	    if (unreadCount <= 0) {
	        message.setRead(true);
	    }

	    chatRepository.save(message);
	    return true;
	}


	@Override
	public List<ChatMessageDTO> findRoomsByIdsAndKeyword(List<String> activeRoomIds, String keyword) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getUnreadCountByRoom(String roomId, String userId) {
	    return chatRepository.getUnreadCountByRoom(roomId, userId);
	}

	@Override
	public int getUnreadCount(String userId) {
	    return chatRepository.getTotalUnreadCount(userId);
	}
	@Override
	public boolean deleteMessage(Long messageId, String username) {
	    Optional<ChatMessageDTO> optMsg = chatRepository.findById(messageId);
	    if (optMsg.isEmpty()) {
	        return false; // 삭제할 메시지 없음
	    }

	    ChatMessageDTO message = optMsg.get();

	    if (!message.getSender().equals(username)) {
	        throw new RuntimeException("본인 메시지만 삭제할 수 있습니다.");
	    }

	    chatRepository.deleteById(messageId);

	    Map<String, Object> payload = new HashMap<>();
	    payload.put("type", "delete");
	    payload.put("messageId", messageId);
	    messagingTemplate.convertAndSend("/topic/chat/room/" + message.getRoomId(), payload);

	    return true;
	}
    /**
     * 메시지 삭제 후 DTO 반환 및 알림 (기존 메서드)
     */
    @Override
    public ChatMessageDTO deleteMessageAndNotify(Long messageId, String username) {
        boolean deleted = deleteMessage(messageId, username);
        if (!deleted) {
            throw new RuntimeException("삭제할 메시지가 없습니다.");
        }

        // 삭제된 메시지 DTO 생성 (알림용)
        ChatMessageDTO dto = new ChatMessageDTO();
        dto.setId(messageId);
        dto.setDeleted(true); // 예: 삭제 표시용 필드

        return dto;
    }

	@Override
	public boolean deleteMessage(Long messageId) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Long getRoomIdByMessageId(Long msgId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ChatMessageDTO> getMessages(String roomId, String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Transactional
	public void save(ChatMessageDTO msg) {
		 chatRepository.save(msg);
		 
		
	}

	@Override
	public List<ChatMessageDTO> load(String sender) {
		// TODO Auto-generated method stub
		return null;
	}

    public List<MeMessageDTO> findMessagesBySender(String sender) {
        return meMessageRepository.findBySenderOrderByCreatedAtAsc(sender);
    }

	@Override
	public MeMessageDTO saveToMyRoom(MeMessageDTO message) {
		 return meMessageRepository.save(message);
	}

	 public boolean forwardMessage(String loginId, Long messageId, String forwardTo) {
	        Optional<ChatMessageDTO> originalOpt = chatRepository.findById(messageId);
	        if (!originalOpt.isPresent()) {
	            return false; // 원본 메시지가 없으면 실패
	        }

	        ChatMessageDTO original = originalOpt.get();

	        // 새 메시지 생성 - 필수값을 모두 설정해야 함
	        ChatMessageDTO forwarded = new ChatMessageDTO();
	        forwarded.setSender(loginId);                    // 전달자: 로그인 사용자
	        forwarded.setRecipient(forwardTo);                // 전달 대상 (recipient 필드)
	        forwarded.setRoomId(forwardTo);                    // roomId는 대상 채팅방명으로 지정
	        forwarded.setContent(original.getContent());      // 메시지 내용 복사 (null 불가)
	        forwarded.setTimestamp(LocalDateTime.now());      // 현재 시간 설정
	        forwarded.setType(original.getType());             // 원본 메시지 타입 복사
	        forwarded.setUnreadCount(1);                       // 기본값으로 설정
	        forwarded.setRead(false);                          // 읽음 여부 초기화
	        forwarded.setDeletedBySender(false);
	        forwarded.setDeletedByRecipient(false);
	        forwarded.setAnnounced(false);

	        try {
	            chatRepository.save(forwarded);
	            return true;
	        } catch (Exception e) {
	            e.printStackTrace();
	            return false;
	        }
	    }

	@Override
	public ChatMessageDTO getLatestForwardedMessage(String senderId, String forwardTo) {
		   return chatRepository.findTopBySenderAndRoomIdOrderByTimestampDesc(senderId, forwardTo);
	}
	 @Override
	    public String generateRoomId(String userId1, String userId2) {
	        List<String> users = Arrays.asList(userId1, userId2);
	        Collections.sort(users);
	        return users.get(0) + "_" + users.get(1);
	    }

	    @Override
	    public List<ChatMessageDTO> getMessagesByRoomId(String userId1, String userId2) {
	        String roomId = generateRoomId(userId1, userId2);
	        return chatRepository.findMessagesByRoomIdOrderByTimestampAsc(roomId);
	    }
}
