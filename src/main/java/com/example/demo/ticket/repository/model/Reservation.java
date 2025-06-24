package com.example.demo.ticket.repository.model;

import java.time.LocalDateTime;

import com.example.demo.ticket.type.ReservationStatus;
import com.example.demo.ticket.type.SeatStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
@Table(name = "reservation_temp")
public class Reservation {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@ManyToOne
    @JoinColumn(name = "SCHEDULE_ID")
	private Schedule schedule;
	
	// 예약된 좌석 (ManyToOne)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SEAT_ID", nullable = false)
    private Seat seat;
    
 // 예약 상태
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SeatStatus seatStatus;
    
    // 예약/잠금 시작 시간
    @Column(name = "lock_time")
    private LocalDateTime lockTime;

    // 예약 확정 시간 (필요 시)
    @Column(name = "confirm_time")
    private LocalDateTime confirmTime;

	@Enumerated(EnumType.STRING)
	private ReservationStatus status; // CONFIRMED, CANCELLED 등


	// 고객 정보, 예약 시간, 결제 상태 등 추가 필드 가능

	// getters, setters
}
