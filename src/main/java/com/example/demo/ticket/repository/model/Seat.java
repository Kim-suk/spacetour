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

@Entity
@Getter
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

    @Column(name = "IS_RESERVED", nullable = false, length = 1)
    private String isReserved = "N"; // default

	public Object getIsBlocked() {
		// TODO Auto-generated method stub
		return null;
	}
}
	