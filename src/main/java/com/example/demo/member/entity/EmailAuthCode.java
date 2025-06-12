package com.example.demo.member.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "email_auth_code")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmailAuthCode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String code;

    private LocalDateTime expireAt;
}