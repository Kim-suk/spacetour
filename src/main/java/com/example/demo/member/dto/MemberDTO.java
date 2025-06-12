package com.example.demo.member.dto;

import java.sql.Date;

import org.springframework.stereotype.Component;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "member")
@Component
@Getter
@Setter
@ToString
public class MemberDTO {
    
    @Id
    private String id;             // 사용자 ID
    
    private String pwd;            // 비밀번호
    
    private String name;           // 사용자 이름 (realName)
    
    private Date birth;            // 생년월일
    
    private String gender;         // 성별
    
    private String phone;          // 전화번호
    
    private String email;          // 이메일 주소
    
    private String postcode;
    private String address;
    private String detailAddress;
    private String extraAddress;
    
    private String phoneAuth;      // 전화번호 인증 여부 (Y/N)
    
    private String agree;          // 약관 동의 여부 (on)
    
    private String profileImage;   // 프로필 이미지
    
    @Column(insertable = false, updatable = false, columnDefinition = "date default sysdate")
    private Date joinDate;         // 가입일

	public MemberDTO selectByIdAndEmail(String id2, String email2) {
		// TODO Auto-generated method stub
		return null;
	}
}
