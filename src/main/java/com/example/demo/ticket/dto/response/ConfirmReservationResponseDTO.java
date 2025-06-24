package com.example.demo.ticket.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import com.example.demo.ticket.type.SeatGrade;
import com.example.demo.ticket.type.TripType;

import lombok.Data;

@Data
public class ConfirmReservationResponseDTO {
	private TripType tripType;
    private int passengerCount;

    // 출발편 정보
    private Long departureScheduleId;
    private String departureSpaceshipName;
    private String departureRoute;
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;
    private SeatGrade departureGrade;
    private List<String> departureSeatIds;

    // 복귀편 정보
    private Long returnScheduleId;
    private String returnSpaceshipName;
    private String returnRoute;
    private LocalDateTime returnTime;
    private LocalDateTime returnArrivalTime;
    private SeatGrade returnGrade;
    private List<String> returnSeatIds;

    // 가격 정보
    private int departurePrice;
    private int returnPrice;
    private int totalPrice;
	
}
