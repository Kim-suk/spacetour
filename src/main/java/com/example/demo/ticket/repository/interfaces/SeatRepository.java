package com.example.demo.ticket.repository.interfaces;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.ticket.repository.model.Seat;
import com.example.demo.ticket.type.SeatGrade;

public interface SeatRepository extends JpaRepository<Seat, Long>{


	List<Seat> findByScheduleIdAndSeatGrade(Long scheduleId, SeatGrade seatGrade);
	List<Seat> findByScheduleId(Long scheduleId);

}
