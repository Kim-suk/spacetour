package com.example.demo.ticket.repository.model;

import java.time.Duration;
import java.time.LocalDateTime;

import com.example.demo.ticket.type.SeatGrade;
import com.example.demo.ticket.type.SeatStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "SEAT")
@SequenceGenerator(name = "seat_seq", sequenceName = "SEAT_SEQ", allocationSize = 1)
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seat_seq")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "SCHEDULE_ID")
    private Schedule schedule;

    @Enumerated(EnumType.STRING)
    @Column(name = "SEAT_GRADE", nullable = false, length = 20)
    private SeatGrade seatGrade;

    @Column(name = "SEAT_NUMBER", nullable = false, length = 10)
    private String seatNumber;

 // 기존 isReserved("Y"/"N") 대신 SeatStatus enum 사용
    @Enumerated(EnumType.STRING)
    @Column(name = "SEAT_STATUS", nullable = false, length = 20)
    private SeatStatus seatStatus = SeatStatus.AVAILABLE;  // 기본값
    
    @Column(name = "X_COORD")
    private Integer x;
    
    @Column(name = "Y_COORD")
    private Integer y;
    
    @Column(name = "SEAT_IMAGE")
    private String seatImage;
    
    @Column(name = "LOCK_TIMESTAMP")
    private LocalDateTime lockTimestamp;
    
 // 임시잠금 만료시간 (예: 5분)
    private static final Duration LOCK_TIMEOUT = Duration.ofMinutes(5);

    // 예약 가능 여부 체크 (임시 잠금 만료 포함)
    public boolean isAvailableForLock() {
        if (seatStatus == SeatStatus.AVAILABLE) {
            return true;
        }
        if (seatStatus == SeatStatus.LOCKED && lockTimestamp != null) {
            return lockTimestamp.plus(LOCK_TIMEOUT).isBefore(LocalDateTime.now());
        }
        return false;
    }
 // 예약 가능 여부 확인 편의 메서드
    public boolean isAvailable() {
    	return this.seatStatus == SeatStatus.AVAILABLE;
    }
}
	