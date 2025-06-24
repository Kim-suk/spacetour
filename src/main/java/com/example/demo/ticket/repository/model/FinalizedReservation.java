package com.example.demo.ticket.repository.model;

import java.time.LocalDateTime;

import com.example.demo.ticket.type.SeatGrade;
import com.example.demo.ticket.type.TripType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "FINALIZEDRESERVATION")
public class FinalizedReservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String loginId; // 결제자 ID

    private String orderId; // 결제 고유번호 (ex: Stripe의 PaymentIntent ID 또는 자체 생성)

    private Long departureScheduleId;

    private Long returnScheduleId;

    @Enumerated(EnumType.STRING)
    private SeatGrade departureSeatGrade;

    @Enumerated(EnumType.STRING)
    private SeatGrade returnSeatGrade;

    @Enumerated(EnumType.STRING)
    private TripType tripType;

    @Column(length = 1000)
    private String departureSeatIds;  // 예: "A01,B01,C01"

    @Column(length = 1000)
    private String returnSeatIds;     // 예: "D01,E01"

    private Integer passengerCount;

    private Integer totalPrice;

    private LocalDateTime reservationDate;

    private String paymentStatus; // 예: "SUCCESS", "FAILED", "PENDING"
}