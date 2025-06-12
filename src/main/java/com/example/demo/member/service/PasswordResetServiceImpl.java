package com.example.demo.member.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class PasswordResetServiceImpl implements PasswordResetService {

    @Autowired
    private JavaMailSender mailSender;

    @Override
    public String resetPassword(String email) {
        String tempPassword = generateTempPassword();

        // DB에서 사용자 찾고 비밀번호 변경하는 로직 필요 (예: MemberDAO.updatePassword(email, encPassword))

        // 이메일 발송
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("임시 비밀번호 안내");
        message.setText("임시 비밀번호는 [" + tempPassword + "] 입니다. 로그인 후 변경해주세요.");
        mailSender.send(message);

        return "임시 비밀번호가 전송되었습니다.";
    }

    private String generateTempPassword() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder();
        Random rnd = new Random();
        for (int i = 0; i < 10; i++) {
            sb.append(chars.charAt(rnd.nextInt(chars.length())));
        }
        return sb.toString();
    }
}
