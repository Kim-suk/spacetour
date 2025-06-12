package com.example.demo.userChat.dto;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "ME_MESSAGES")
@Getter
@Setter
public class MeMessageDTO {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "me_msg_seq")
    @SequenceGenerator(name = "me_msg_seq", sequenceName = "me_messages_seq", allocationSize = 1)
    private Long id;

    @Column(nullable = false)
    private String sender;
    
    @Column(nullable = true)
    private String receiver;
    @Lob
    @Column(nullable = false)
    private String content;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    public MeMessageDTO() {}

    public MeMessageDTO(Long id, String sender, String content, LocalDateTime createdAt) {
        this.id = id;
        this.sender = sender;
        this.content = content;
        this.createdAt = createdAt;
    }

    public void setSendTime(LocalDateTime now) {
        this.createdAt = now;
    }

	public String getReceiver() {
		// TODO Auto-generated method stub
		return null;
	}
}
