package com.example.demo.ticket.dto.request;

import java.time.LocalDate;
import com.example.demo.ticket.type.TripType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScheduleRequestDTO {
    private String departure;		// 출발지
    private String destination;		// 도착지
    private LocalDate departureDate;// 출발일
    private LocalDate returnDate;	// 왕복일(편도일 경우 null)
    private TripType tripType;		// TripType.ONEWAY or ROUNDTRIP	
    private int passengerCount;		// 인원 수

}
