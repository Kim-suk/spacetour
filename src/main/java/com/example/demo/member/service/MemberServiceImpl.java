package com.example.demo.member.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.member.dto.MemberDTO;
import com.example.demo.member.repository.MemberRepository;
import com.example.demo.order.dto.OrderDTO;
import com.example.demo.order.repository.OrderRepository;
import com.example.demo.reserve.dto.ReserveDTO;
import com.example.demo.reserve.repository.ReserveRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class MemberServiceImpl implements MemberService{
	@Autowired
	MemberRepository repository;
	
	@Autowired
	ReserveRepository reserverepository;
	
	 @Autowired
	OrderRepository orderRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Override
	public List<MemberDTO> findAll() {
		// TODO Auto-generated method stub
		return repository.findAll();
	}

	@Override
	public void save(MemberDTO dto) {
		// TODO Auto-generated method stub
		String encodedPwd = passwordEncoder.encode(dto.getPwd());
		dto.setPwd(encodedPwd);
		repository.save(dto);
	}

	@Override
	public MemberDTO findById(String id) {
		// TODO Auto-generated method stub
		return repository.findById(id).orElseThrow(() -> new IllegalArgumentException("회원정보가 없습니다."));
	}

	/*
	 * @Override public int merge(MemberDTO dto) { // TODO Auto-generated method
	 * stub int result = repository.updateMember(dto.getId(), dto.getPwd(),
	 * dto.getName(), dto.getEmail()); return result; }
	 */

	@Override
	public int remove(String id) {
		// TODO Auto-generated method stub
		int result = repository.deleteMember(id);
		return result;
	}

	@Override
	public boolean login(String id, String pwd) {
	    String inputPwd = pwd != null ? pwd.trim() : "";
	    Optional<MemberDTO> memberOpt = repository.findById(id);

	    if (memberOpt.isPresent()) {
	        MemberDTO member = memberOpt.get();
	        String encodedPwd = member.getPwd();

	        // 👉 로그 출력 시도 (inputPwd로 출력하는 게 정확)
	        System.out.println("입력 비번(trim 적용): " + inputPwd);
	        System.out.println("DB 비번: " + encodedPwd);

	        if (passwordEncoder.matches(inputPwd, encodedPwd)) {  // <-- pwd → inputPwd 변경
	            return true;
	        }
	    }
	    return false;
	}


	@Override
	public List<ReserveDTO> findReservationsByMemberId(String loginId) {
		return reserverepository.findByLoginId(loginId);
	}	

	@Override
	public List<OrderDTO> findOrdersByMemberId(String loginId) {
		return orderRepository.findByMemberId(loginId);
	}

	@Override
	public int updateMember(MemberDTO dto) {
		return repository.updateMember(dto.getId(), dto.getName(), dto.getEmail(), dto.getPhone(), dto.getAddress(), dto.getProfileImage());
	}

	@Override
	public int updateProfileImg(String id, String filename) {
		return repository.updateProfileImage(id, filename);
	}

	@Override
	public void insertMember(MemberDTO dto) {
		repository.save(dto);
	}

	@Override
	public boolean isIdDuplicate(String id) {
	    return repository.existsById(id);
	}

	@Override
	public boolean existsById(String id) {
		// TODO Auto-generated method stub
		return repository.existsById(id);
	}

	@Override
	public boolean changePassword(String loginId, String currentPassword, String newPassword) {
		// TODO Auto-generated method stub
		MemberDTO member = findById(loginId);
		
		if(!passwordEncoder.matches(currentPassword, member.getPwd())){
			return false; // 현재 비밀번호가 일치하지 않음
		}
		
		String encodeNewPwd = passwordEncoder.encode(newPassword);
		member.setPwd(encodeNewPwd);
		repository.save(member); //업데이트 저장
		
		return true;
	}

	@Override
	public String findIdByEmail(String name, String email) {
	    return repository.findByNameAndEmail(name, email)
	                     .map(MemberDTO::getId)
	                     .orElse(null);
	}

	@Override
	public String findUserIdByEmail(String email) {
	    return repository.findByEmail(email)
	                     .map(MemberDTO::getId)
	                     .orElse(null);
	}

	@Override
	public MemberDTO findByNameAndEmail(String name, String email) {
		  return repository.findByNameAndEmail(name, email).orElse(null);
	}

	@Override
	public void sendIdEmail(String email, String id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String findUserIdByEmailAndName(String email, String name) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isEmailExists(String email) {
        return repository.existsByEmail(email);
    }
}	



