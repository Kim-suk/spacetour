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
@Table(name = "CHAT_MESSAGES")
@Getter
@Setter
public class ChatMessageDTO {

	public enum MessageType {
	    ENTER, TALK, LEAVE, TEXT, IMAGE, CHAT;
	}

	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "chat_seq")
    @SequenceGenerator(name = "chat_seq", sequenceName = "chat_messages_seq", allocationSize = 1)
    private Long id;
	
	  @Column(name = "unread_count", nullable = false)
	    private int unreadCount = 1;  // 기본값 1
	
    @Column(nullable = false)
    private String sender;

    @Column(nullable = false)
    private String recipient;

    @Column(name = "room_id", nullable = false)
    private String roomId;

    @Lob
    @Column(nullable = false)
    private String content;

    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;

    @Column(name = "is_read")
    private Boolean read = false;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "chat_message_readers", joinColumns = @JoinColumn(name = "message_id"))
    @Column(name = "reader")
    private Set<String> readByUsers = new HashSet<>();

    @Column(name = "deleted_by_sender")
    private Boolean deletedBySender = false;

    @Column(name = "deleted_by_recipient")
    private Boolean deletedByRecipient = false;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MessageType type;

    @Column(name = "image_url")
    private String imageUrl;

    // 공지 여부 컬럼 추가
    @Column(name = "announced")
    private Boolean announced = false;

    @Transient
    private boolean deleted = false; // 프론트용

    public ChatMessageDTO() {
        this.timestamp = LocalDateTime.now();
    }

    public ChatMessageDTO(String sender, String recipient, String content, String roomId) {
        this.sender = sender;
        this.recipient = recipient;
        this.content = content;
        this.roomId = roomId;
        this.timestamp = LocalDateTime.now();
        this.unreadCount = 1;  // 메시지 생성시 1로 설정
        this.announced = false;
    }
    // 메시지 읽음 처리용 메서드 추가 가능
    public void markAsRead() {
        this.unreadCount = 0;
        this.read = true;
    }
    // 삭제 가능 시간 확인 (5분 이내)
    public boolean canDelete() {
        LocalDateTime now = LocalDateTime.now();
        Duration duration = Duration.between(this.timestamp, now);
        return duration.toMinutes() < 5;
    }

    public boolean isImageMessage() {
        return this.imageUrl != null && !this.imageUrl.isBlank();
    }

    // announced getter/setter
    public Boolean getAnnounced() {
        return this.announced;
    }

    public void setAnnounced(Boolean announced) {
        this.announced = announced;
    }

    // --- 읽음 처리 메서드 ---
    // 특정 사용자가 메시지를 읽었는지 여부 확인
    public boolean isReadByUser(String sender) {
        return readByUsers.contains(sender);
    }

    // 읽음 처리 메서드
    public void markAsReadBy(String userId) {
        if (this.readByUsers == null) {
            this.readByUsers = new HashSet<>();
        }
        if (!this.readByUsers.contains(userId)) {
            this.readByUsers.add(userId);
            // 내가 보낸 메시지일 경우 상대가 읽으면 unreadCount를 0으로
            if (!userId.equals(this.sender)) {
                this.unreadCount = 0;
                this.read = true;
            }
        }
    }

	public Map<String, Object> getSessionAttributes() {
		// TODO Auto-generated method stub
		return null;
	}

	public int getTotalRecipients() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void setReceiver(String forwardTo) {
		// TODO Auto-generated method stub
		
	}

	public void setType(MessageType type) {
	    this.type = type;
	}

	public void setType(String typeStr) {
	    if(typeStr == null) {
	        this.type = null;
	        return;
	    }
	    try {
	        this.type = MessageType.valueOf(typeStr.toUpperCase());
	    } catch (IllegalArgumentException e) {
	        this.type = null; // 또는 기본값 지정
	    }
	}

}
