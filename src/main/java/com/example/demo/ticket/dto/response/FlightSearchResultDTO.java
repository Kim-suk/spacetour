package com.example.demo.ticket.dto.response;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FlightSearchResultDTO {
	private List<FlightResponseDTO> departureFlights;
	private List<FlightResponseDTO> returnFlights;
}
