package com.example.demo.ticket.repository.interfaces;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.ticket.repository.model.Schedule;
import com.example.demo.ticket.repository.model.Seat;
import com.example.demo.ticket.type.SeatGrade;
import com.example.demo.ticket.type.SeatStatus;

public interface SeatRepository extends JpaRepository<Seat, Long>{


	 List<Seat> findByScheduleIdAndSeatGrade(Schedule schedule, SeatGrade seatGrade);

	 List<Seat> findByScheduleId(Long scheduleId);

	 List<Seat> findByScheduleIdAndIdIn(Schedule schedule, List<Long> ids);

	 Seat findByScheduleIdAndId(Schedule schedule, Long id);
	    
	 Seat findByScheduleIdAndSeatNumber(Schedule schedule, String seatNumber);

	 @Query("SELECT s FROM Seat s WHERE s.seatStatus = :status AND s.lockTimestamp <= :expiryTime")
	 List<Seat> findLockedSeatsLockedBefore(@Param("status") SeatStatus status,
	                                        @Param("expiryTime") LocalDateTime expiryTime);

	 // 여기서 필드명 'lockTimestamp'로 수정했습니다.
	 List<Seat> findBySeatStatusAndLockTimestampBefore(SeatStatus seatStatus, LocalDateTime lockTimestamp);

	 Optional<Seat> findBySeatNumberAndScheduleId(String seatNumber, Long scheduleId);


	
	

}
