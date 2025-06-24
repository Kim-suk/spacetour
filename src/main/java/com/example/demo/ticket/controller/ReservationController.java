package com.example.demo.ticket.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.ticket.dto.request.ConfirmReservationRequestDTO;
import com.example.demo.ticket.dto.response.ConfirmReservationResponseDTO;
import com.example.demo.ticket.service.ReservationService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/reservation")
@RequiredArgsConstructor
public class ReservationController {
	
	private final ReservationService reservationService;
	
	@PostMapping("/confirmReservation")
	public String confirmReservation(@ModelAttribute ConfirmReservationRequestDTO requestDto,
	                                 Model model,
	                                 HttpSession session) {
	    System.out.println("✅ [LOG] departureScheduleId = " + requestDto.getDepartureScheduleId());
	    System.out.println("✅ [LOG] departureGrade = " + requestDto.getDepartureSeatGrade());
	    System.out.println("✅ [LOG] returnScheduleId = " + requestDto.getReturnScheduleId());
	    System.out.println("✅ [LOG] returnGrade = " + requestDto.getReturnSeatGrade());
	    System.out.println("✅ [LOG] tripType = " + requestDto.getTripType());
	    System.out.println("✅ [LOG] departureSeatIds = " + requestDto.getDepartureSeatIds());
	    System.out.println("✅ [LOG] returnSeatIds = " + requestDto.getReturnSeatIds());

	    // 예약 데이터 준비
	    ConfirmReservationResponseDTO responseDto = reservationService.prepareReservation(requestDto);

	    // 1️⃣ 모델에 전달 (현재 화면 렌더링용)
	    model.addAttribute("reservation", responseDto);

	    // 2️⃣ 세션에도 저장 (결제 후 조회용)
	    session.setAttribute("reservation", responseDto);

	    return "reservation/confirmReservation";
	}
	
	
	
	
	
}
