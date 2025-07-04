package com.example.demo.member.repository;

import com.example.demo.member.entity.EmailAuthCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmailAuthCodeRepository extends JpaRepository<EmailAuthCode, Long> {
    Optional<EmailAuthCode> findTopByEmailOrderByExpireAtDesc(String email);
}
