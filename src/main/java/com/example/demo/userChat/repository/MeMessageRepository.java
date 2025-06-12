package com.example.demo.userChat.repository;
	
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;	
import org.springframework.stereotype.Repository;
import com.example.demo.userChat.dto.MeMessageDTO;
	
	
	@Repository
	public interface MeMessageRepository extends JpaRepository<MeMessageDTO, Long> {

		List<MeMessageDTO> findBySenderOrderByCreatedAtAsc(String sender);
	}
