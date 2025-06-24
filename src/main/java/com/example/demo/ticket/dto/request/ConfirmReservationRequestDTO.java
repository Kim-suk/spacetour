package com.example.demo.ticket.dto.request;



import java.util.List;

import com.example.demo.ticket.type.SeatGrade;
import com.example.demo.ticket.type.TripType;

import lombok.Data;

@Data
public class ConfirmReservationRequestDTO {
	private Long departureScheduleId;
    private List<String> departureSeatIds; // CSV: "A01,A02"
    private Long returnScheduleId;
    private List<String> returnSeatIds; // CSV
    private SeatGrade departureSeatGrade;
    private SeatGrade returnSeatGrade;
    private TripType tripType;
    private int passengerCount;
}
