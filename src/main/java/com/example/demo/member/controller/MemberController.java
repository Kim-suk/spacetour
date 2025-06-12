package com.example.demo.member.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.member.dto.MemberDTO;

import jakarta.servlet.http.HttpSession;

public interface MemberController {
   String memberList(Model model);
   String joinMemberForm(@PathVariable("formName") String formName);
   String joinMember(MemberDTO dto, Model model);
   String detailMember(@RequestParam("id") String id, Model model);

	/*
	 * String updateMember(MemberDTO dto, Model model);
	 */ 
   String deleteMember(@RequestParam("id") String id, Model model);
   String login(@RequestParam("id") String id, @RequestParam("pwd") String pwd, Model model, HttpSession session);
   String logout( Model model, HttpSession session);
   String manageMember(Model model, HttpSession session);
   
   // 비밀번호 변경
   public String showChangePassword();
   public String changePassword(@RequestParam("currentPassword") String currentPassword, @RequestParam("newPassword") String newPassword, @RequestParam("confirmPassword") String confirmPassword, HttpSession session, Model model);
}