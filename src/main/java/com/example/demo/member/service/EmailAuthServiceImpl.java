package com.example.demo.member.service;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import com.example.demo.member.dto.MemberDTO;
import com.example.demo.member.entity.EmailAuthCode;
import com.example.demo.member.repository.EmailAuthCodeRepository;
import com.example.demo.member.repository.MemberRepository;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class EmailAuthServiceImpl implements EmailAuthService {

    @Autowired
    private JavaMailSender mailSender;
    
    @Autowired
    private MemberService service;


	@Override
	public String findIdByEmail(String name, String email) {
	    MemberDTO member = service.findByNameAndEmail(name, email);
	    return (member != null) ? member.getId() : null;
	}


	public String sendUserIdByEmail(String email, String userId) {
	    SimpleMailMessage message = new SimpleMailMessage();
	    message.setTo(email);
	    message.setSubject("회원님의 아이디 안내");
	    message.setText("요청하신 아이디는 다음과 같습니다: [" + userId + "]");
	    mailSender.send(message);
	    return "아이디가 이메일로 전송되었습니다.";
	}

	
	@Override
	public String findUserIdByEmail(String email) {
		// TODO Auto-generated method stub
		return null;
	}




}