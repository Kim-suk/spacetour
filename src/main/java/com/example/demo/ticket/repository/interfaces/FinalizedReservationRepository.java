package com.example.demo.ticket.repository.interfaces;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.ticket.repository.model.FinalizedReservation;

public interface FinalizedReservationRepository extends JpaRepository<FinalizedReservation, Long>{
	List<FinalizedReservation> findByLoginId(String loginId);
}
