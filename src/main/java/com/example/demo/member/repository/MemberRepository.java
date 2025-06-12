package com.example.demo.member.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.member.dto.MemberDTO;

import jakarta.transaction.Transactional;

public interface MemberRepository extends JpaRepository<MemberDTO, String> {

    @Modifying
    @Transactional
    @Query("update MemberDTO m set m.name = :name, m.email= :email, m.phone = :phone, m.address = :address, m.profileImage = :profileImage where m.id = :id")
    int updateMember(@Param("id") String id, 
    				 @Param("name") String name,
                     @Param("email") String email, 
                     @Param("phone") String phone,
                     @Param("address") String address, 
                     @Param("profileImage") String profileImage);

    @Modifying
    @Transactional
    @Query("delete from MemberDTO m where m.id = :id")
    int deleteMember(@Param("id") String id);

	/*
	 * Optional<MemberDTO> findByIdAndPwd(String id, String pwd);
	 */
    
    boolean existsById(String id);
	
	  @Modifying
	  @Transactional
	  @Query("update MemberDTO m set m.profileImage = :filename where m.id = :id")
	  int updateProfileImage(@Param("id") String id, @Param("filename") String filename);

		/*
		 * int updateMember(String id, String pwd, String name, String email);
		 */		    
	  Optional<MemberDTO> findByNameAndEmail(String name, String email);
	  Optional<MemberDTO> findByEmail(String email);
	  
	  
	  boolean existsByEmail(String email); // 이메일 중복 여부 확인
}
