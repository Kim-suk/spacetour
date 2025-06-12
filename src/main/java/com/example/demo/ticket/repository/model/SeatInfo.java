package com.example.demo.ticket.repository.model;

import com.example.demo.ticket.type.SeatGrade;

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
@Table(name = "SEAT_INFO")
@Getter
@Setter
@SequenceGenerator(name = "seat_info_seq", sequenceName = "SEAT_INFO_SEQ", allocationSize = 1)
public class SeatInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seat_info_seq")
    private Long id;	

    @ManyToOne
    @JoinColumn(name = "SCHEDULE_ID")
    private Schedule schedule;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "SEAT_GRADE", nullable = false, length = 20)
    private SeatGrade seatGrade;

    @Column(nullable = true)
    private Integer price;  // null이면 기본 가격 사용

    @Column(name = "REMAINING_SEATS", nullable = false)
    private Integer remainingSeats;
    
    @Column(name = "OPERATED", nullable = false)
    private boolean operated = false;  // 기존 String "Y"/"N" → boolean true/false
}
