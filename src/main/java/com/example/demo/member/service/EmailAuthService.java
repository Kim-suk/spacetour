package com.example.demo.member.service;

public interface EmailAuthService {

    String findUserIdByEmail(String email);
   String findIdByEmail(String name, String email);	
	 String sendUserIdByEmail(String email, String userId);  // ✅ 이거 선언했는지 확인
}
