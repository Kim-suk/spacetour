	package com.example.demo.userChat.repository;
	
	import java.util.List;
	
	import org.springframework.data.domain.Pageable;
	import org.springframework.data.jpa.repository.JpaRepository;
	import org.springframework.data.jpa.repository.Modifying;
	import org.springframework.data.jpa.repository.Query;
	import org.springframework.data.repository.query.Param;
	
	import com.example.demo.userChat.dto.ChatMessageDTO;
	
	import jakarta.transaction.Transactional;
	
	public interface ChatRepository extends JpaRepository<ChatMessageDTO, Long> {
	
	    // 1. 채팅방 내 전체 메시지 조회 (오름차순)
	    List<ChatMessageDTO> findByRoomIdOrderByTimestampAsc(String roomId);
	
	    // 2. 사용자 간 전체 대화 조회
	    @Query("SELECT c FROM ChatMessageDTO c " +
	           "WHERE (c.sender = :sender AND c.recipient = :recipient) " +
	           "   OR (c.sender = :recipient AND c.recipient = :sender) " +
	           "ORDER BY c.timestamp ASC")
	    List<ChatMessageDTO> findChatBetweenUsers(@Param("sender") String sender,
	                                              @Param("recipient") String recipient);
	
	    // 3. 두 사용자 간 모든 메시지 (Spring Method Style)
	    List<ChatMessageDTO> findBySenderAndRecipientOrRecipientAndSenderOrderByTimestampAsc(String sender,
				String recipient, String sender2, String recipient2);
	
	    // 4. 채팅방 메시지를 읽음 처리
	    @Modifying
	    @Transactional
	    @Query("UPDATE ChatMessageDTO m SET m.read = true " +
	           "WHERE m.roomId = :roomId AND m.recipient = :userId AND m.read = false")
	    void markMessagesAsRead(@Param("roomId") String roomId, @Param("userId") String userId);
	
	    // 5. 채팅방별 읽지 않은 메시지 수
	    @Query("SELECT m.roomId, COUNT(m) FROM ChatMessageDTO m " +
	           "WHERE m.recipient = :userId AND m.read = false " +
	           "GROUP BY m.roomId")
	    List<Object[]> countUnreadMessagesByUser(@Param("userId") String userId);
	
	    // 6. 삭제되지 않은 메시지 조회 (논리 삭제 체크)
	    @Query("SELECT m FROM ChatMessageDTO m WHERE m.roomId = :roomId AND " +
	           "((m.sender = :userId AND m.deletedBySender = false) " +
	           "OR (m.recipient = :userId AND m.deletedByRecipient = false)) " +
	           "ORDER BY m.timestamp ASC")
	    List<ChatMessageDTO> findAllByRoomIdAndUserNotDeleted(@Param("roomId") String roomId,
	                                                          @Param("userId") String userId);
	
	    // 7. 사용자가 포함된 채팅방 목록 (중복 제거)
	    @Query("SELECT DISTINCT m.roomId FROM ChatMessageDTO m " +
	           "WHERE m.sender = :userId OR m.recipient = :userId " +
	           "ORDER BY m.roomId ASC")
	    List<String> findDistinctRoomsByUser(@Param("userId") String userId);
	
	    // 8. 채팅방의 마지막 메시지 조회 (최신 메시지 여러개 가능성 있음)
	    @Query("SELECT m FROM ChatMessageDTO m " +
	           "WHERE m.roomId = :roomId " +
	           "ORDER BY m.timestamp DESC")
	    List<ChatMessageDTO> findLastMessageInRoom(@Param("roomId") String roomId);
	
	    // 9. 사용자의 채팅방 목록을 마지막 메시지 기준으로 정렬
	    @Query("SELECT m FROM ChatMessageDTO m " +
	           "WHERE m.timestamp IN (" +
	           "   SELECT MAX(m2.timestamp) FROM ChatMessageDTO m2 " +
	           "   WHERE m2.sender = :userId OR m2.recipient = :userId " +
	           "   GROUP BY m2.roomId" +
	           ") ORDER BY m.timestamp DESC")
	    List<ChatMessageDTO> findUserRoomsOrderByLatestMessage(@Param("userId") String userId);
	
	    // 10. 검색어로 채팅방 필터링 (방 ID 검색)
	    @Query("SELECT m FROM ChatMessageDTO m " +
	           "WHERE (m.sender = :userId OR m.recipient = :userId) " +
	           "AND (:keyword IS NULL OR m.roomId LIKE %:keyword%) " +
	           "AND m.timestamp IN (" +
	           "   SELECT MAX(m2.timestamp) FROM ChatMessageDTO m2 " +
	           "   WHERE m2.sender = :userId OR m2.recipient = :userId " +
	           "   GROUP BY m2.roomId" +
	           ") ORDER BY m.timestamp DESC")
	    List<ChatMessageDTO> findRoomsByUserWithKeyword(@Param("userId") String userId,
	                                                    @Param("keyword") String keyword);
	
	    // 11. 페이징 처리하여 채팅방 이전 메시지 조회 (내림차순, 이후 서비스에서 Collections.reverse() 처리)
	    @Query("SELECT m FROM ChatMessageDTO m " +
	           "WHERE m.roomId = :roomId " +
	           "ORDER BY m.timestamp DESC")
	    List<ChatMessageDTO> findPreviousMessagesByRoomId(@Param("roomId") String roomId, Pageable pageable);
	
	    // 12. 수신자 기준 중복 채팅방 목록
	    @Query("SELECT DISTINCT m.roomId FROM ChatMessageDTO m " +
	           "WHERE m.recipient = :userId")
	    List<String> findDistinctRoomsByRecipient(@Param("userId") String userId);
	
	    // 13. 발신자 기준 메시지 조회
	    List<ChatMessageDTO> findBySender(String sender);
	
	    @Query("SELECT c FROM ChatMessageDTO c WHERE (c.sender = :userId OR c.recipient = :userId) AND c.roomId LIKE %:keyword%")
	    List<ChatMessageDTO> findRoomsByUserAndKeyword(@Param("userId") String userId, @Param("keyword") String keyword);
	
	    @Query("SELECT c FROM ChatMessageDTO c WHERE c.roomId = :roomId AND (c.sender = :name1 OR c.recipient = :name2) ORDER BY c.timestamp ASC")
	    List<ChatMessageDTO> findByRoomIdAndSenderOrRecipient(@Param("roomId") String roomId,
	                                                         @Param("name1") String name1,
	                                                         @Param("name2") String name2);
	
	 // 1. 공지사항 1건 조회 (최신 공지)
	    List<ChatMessageDTO> findTop1ByAnnouncedTrueOrderByTimestampDesc();
	
	    default ChatMessageDTO findAnnouncedMessage() {
	        List<ChatMessageDTO> messages = findTop1ByAnnouncedTrueOrderByTimestampDesc();
	        return messages.isEmpty() ? null : messages.get(0);
	    }
	
	    // 2. 메시지 읽음 처리 (특정 메시지, 수신자 기준)
	    @Modifying
	    @Transactional
	    @Query("UPDATE ChatMessageDTO c SET c.read = true WHERE c.id = :messageId AND c.recipient = :userId")
	    int updateMessageReadStatus(@Param("messageId") Long messageId, @Param("userId") String userId);
	
	    // 3. 특정 채팅방에서 수신자 기준 읽지 않은 메시지 수
	    @Query("SELECT COUNT(m) FROM ChatMessageDTO m " +
	           "WHERE m.roomId = :roomId AND :userId NOT MEMBER OF m.readByUsers AND m.recipient = :userId")
	    long countUnreadMessagesByRoomId(@Param("roomId") String roomId, @Param("userId") String userId);
	
	    // 4. 여러 채팅방에서 수신자 기준 읽지 않은 메시지 수 그룹 조회
	    @Query("SELECT m.roomId, COUNT(m) FROM ChatMessageDTO m " +
	           "WHERE :userId NOT MEMBER OF m.readByUsers AND m.recipient = :userId " +
	           "GROUP BY m.roomId")
	    List<Object[]> countUnreadMessagesGroupByRoom(@Param("userId") String userId);
	
	    // 5. 특정 채팅방에서 읽지 않은 메시지 목록 조회
	    @Query("SELECT m FROM ChatMessageDTO m WHERE m.roomId = :roomId AND :userId NOT MEMBER OF m.readByUsers")
	    List<ChatMessageDTO> findByRoomIdAndReadByUsersNotContaining(@Param("roomId") String roomId, @Param("userId") String userId);
	
	    @Query(value = """
	    	    SELECT * FROM chat_messages m
	    	    WHERE (m.room_id, m.timestamp) IN (
	    	        SELECT room_id, MAX(timestamp)
	    	        FROM chat_messages
	    	        WHERE sender = :userId OR recipient = :userId
	    	        GROUP BY room_id
	    	    )
	    	    ORDER BY m.timestamp DESC
	    	    """, nativeQuery = true)
	    	List<ChatMessageDTO> findsenderRoomsOrderByLatestMessage(@Param("sender") String sender);
	
	    @Query("SELECT COUNT(m) FROM ChatMessageDTO m " +
	    	       "WHERE m.roomId = :roomId AND m.recipient = :userId AND m.read = false")
	    	int getUnreadCountByRoom(@Param("roomId") String roomId, @Param("userId") String userId);
	
	    @Query("SELECT COUNT(m) FROM ChatMessageDTO m " +
	    	       "WHERE m.recipient = :userId AND m.read = false")
	    	int getTotalUnreadCount(@Param("userId") String userId);
	    @Query("SELECT COUNT(m) FROM ChatMessageDTO m WHERE m.recipient = :userId AND m.read = false AND m.deletedByRecipient = false")
	    int countUnreadMessagesForUser(@Param("userId") String userId);

		List<ChatMessageDTO> findBySenderAndRoomId(String sender, String string);

		ChatMessageDTO findTopBySenderAndRoomIdOrderByTimestampDesc(String senderId, String forwardTo);

		List<ChatMessageDTO> findMessagesByRoomIdOrderByTimestampAsc(String roomId);
	  

		
	}