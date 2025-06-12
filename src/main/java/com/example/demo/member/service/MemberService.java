package com.example.demo.member.service;

import java.util.List;

import com.example.demo.member.dto.MemberDTO;
import com.example.demo.order.dto.OrderDTO;
import com.example.demo.reserve.dto.ReserveDTO;

public interface MemberService {
	List<MemberDTO> findAll();
	void save(MemberDTO dto);
	MemberDTO findById(String id);

	/*
	 * int merge(MemberDTO dto);
	 */	
	int remove(String id);

	boolean login(String id, String pwd);
	List<ReserveDTO> findReservationsByMemberId(String loginId);
	List<OrderDTO> findOrdersByMemberId(String loginId);
	int updateMember(MemberDTO dto);
	int updateProfileImg(String id, String filename);
	void insertMember(MemberDTO dto);
	boolean isIdDuplicate(String id);
	boolean existsById(String id);
	
	// 비밀번호 변경
	boolean changePassword(String loginId, String currentPassword, String newPassword);
	String findUserIdByEmail(String email);
	String findIdByEmail(String name, String email);
	MemberDTO findByNameAndEmail(String name, String email);
	void sendIdEmail(String email, String id);
	String findUserIdByEmailAndName(String email, String name);
	boolean isEmailExists(String email);
}