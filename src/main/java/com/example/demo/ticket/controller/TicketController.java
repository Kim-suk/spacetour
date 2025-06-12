package com.example.demo.ticket.controller;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.demo.ticket.dto.request.ScheduleRequestDTO;
import com.example.demo.ticket.dto.response.FlightResponseDTO;
import com.example.demo.ticket.dto.response.SeatMapResponseDto;
import com.example.demo.ticket.service.ScheduleService;
import com.example.demo.ticket.type.SeatGrade;
import com.example.demo.ticket.type.TripType;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/ticket")
@SessionAttributes("request") // ★ 이 이름으로 세션에 저장됨
public class TicketController {
	
	
	@Autowired
	private ObjectMapper objectMapper;
	
	private final ScheduleService scheduleService;
	
	@GetMapping("/selectSchedule")
	public String selectTicketOptionPage(Model model) {
		 model.addAttribute("scheduleRequestDTO", new ScheduleRequestDTO());
		return "ticket/selectSchedule";
	}

	
	// ✅ 항공편 결과 조회 (출발편 + 복귀편)
	@PostMapping("/selectFlight")
	public String selectFlight(@ModelAttribute ScheduleRequestDTO scheduleRequestDTO, Model model, HttpSession session) {
	    // 출발편 조회
	    List<FlightResponseDTO> flightList = scheduleService.findFlightsByCondition(scheduleRequestDTO);
	    flightList.forEach(flight -> {
	        flight.calculateDuration();

	        // 좌석 정보 조회 후 세팅
	        List<FlightResponseDTO.SeatInfoDTO> seatInfoList = scheduleService.findSeatInfoByScheduleId(flight.getScheduleId());
	        flight.setSeatInfoList(seatInfoList);
	    });

	    model.addAttribute("flightList", flightList);
	    model.addAttribute("request", scheduleRequestDTO);
	    session.setAttribute("request", scheduleRequestDTO);

	    if (scheduleRequestDTO.getTripType() != null && scheduleRequestDTO.getTripType() == TripType.ROUNDTRIP) {
	        ScheduleRequestDTO returnRequest = ScheduleRequestDTO.builder()
	            .departure(scheduleRequestDTO.getDestination())
	            .destination(scheduleRequestDTO.getDeparture())
	            .departureDate(scheduleRequestDTO.getReturnDate())
	            .tripType(scheduleRequestDTO.getTripType())
	            .passengerCount(scheduleRequestDTO.getPassengerCount())
	            .build();

	        List<FlightResponseDTO> returnFlightList = scheduleService.findFlightsByCondition(returnRequest);
	        returnFlightList.forEach(flight -> {
	            flight.calculateDuration();

	            List<FlightResponseDTO.SeatInfoDTO> seatInfoList = scheduleService.findSeatInfoByScheduleId(flight.getScheduleId());
	            flight.setSeatInfoList(seatInfoList);
	        });

	        model.addAttribute("returnFlightList", returnFlightList);
	    }

	    model.addAttribute("seatGrades", SeatGrade.values());
	    return "ticket/selectFlight";
	}

    
    @PostMapping("/selectSeat")
    public String selectSeat(@ModelAttribute("request") ScheduleRequestDTO requestDTO,
                             @RequestParam(name = "departureScheduleId", required = false) Long departureScheduleId,
                             @RequestParam(name = "returnScheduleId", required = false) Long returnScheduleId,
                             @RequestParam(name = "departureSeatGrade", required = false) SeatGrade departureSeatGrade,
                             @RequestParam(name = "returnSeatGrade", required = false) SeatGrade returnSeatGrade,
                             Model model) throws JsonProcessingException {

        SeatMapResponseDto departureMap = null;
        SeatMapResponseDto returnMap = null;

        if (departureScheduleId != null) {
            departureMap = scheduleService.getSeatMapAllGrades(departureScheduleId);
            FlightResponseDTO departureFlight = scheduleService.getFlightByScheduleId(departureScheduleId);
            model.addAttribute("departureMap", departureMap);
            model.addAttribute("departureFlight", departureFlight);
        }

        if (requestDTO.getTripType() == TripType.ROUNDTRIP && returnScheduleId != null) {
            returnMap = scheduleService.getSeatMapAllGrades(returnScheduleId);
            FlightResponseDTO returnFlight = scheduleService.getFlightByScheduleId(returnScheduleId);
            model.addAttribute("returnMap", returnMap);
            model.addAttribute("returnFlight", returnFlight);
        }

        // JSON 문자열 대신 SeatLayout 객체를 그대로 모델에 넣기
        if (departureMap != null) {
            model.addAttribute("departureMapJson", departureMap.getSeatLayout());
        } else {
            model.addAttribute("departureMapJson", Collections.emptyList());
        }
        
        if (returnMap != null) {
            model.addAttribute("returnMapJson", returnMap.getSeatLayout());
        } else {
            model.addAttribute("returnMapJson", Collections.emptyList());
        }

        model.addAttribute("departureSeatGrade", departureSeatGrade);
        model.addAttribute("returnSeatGrade", returnSeatGrade);
        model.addAttribute("request", requestDTO);
        
        System.out.println("Trip Type: " + requestDTO.getTripType());
        System.out.println("Return Schedule ID: " + returnScheduleId);
        System.out.println("ReturnMap: " + returnMap);  // 전체 객체 출력
        System.out.println("Return Seat Layout JSON: " + new ObjectMapper().writeValueAsString(returnMap.getSeatLayout()));

        return "ticket/selectSeat";
    }
}
