package com.example.demo.ticket.controller;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.demo.ticket.dto.request.ConfirmReservationRequestDTO;
import com.example.demo.ticket.dto.request.ScheduleRequestDTO;
import com.example.demo.ticket.dto.response.ConfirmReservationResponseDTO;
import com.example.demo.ticket.dto.response.FlightResponseDTO;
import com.example.demo.ticket.dto.response.FlightSearchResultDTO;
import com.example.demo.ticket.dto.response.SeatMapResponseDto;
import com.example.demo.ticket.dto.response.SeatStatusResponseDto;
import com.example.demo.ticket.service.ReservationService;
import com.example.demo.ticket.service.ScheduleService;
import com.example.demo.ticket.type.SeatGrade;
import com.example.demo.ticket.type.TripType;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller				// Spring MVC에서 이 클래스를 웹 요청 처리 컨트롤러로 등록함.( 뷰 이름 반환 -> HTML렌더링 )
@RequiredArgsConstructor  // final 이나 @NonNull 필드를 대상으로 생성자 자동 생성 -> 의존성 주입을 간편하게 처리
@RequestMapping("/ticket") // 이 컨트롤러는 /ticket으로 시작하는 URL 요청을 처리함.
@SessionAttributes("request") // ★ 이 이름으로 세션에 저장됨 "request"라는 이름으로 등록된 모델 속성은 세션에 저장됨(다음 요청에서도 유지됨)
public class TicketController {

	/**
	 * 작성자 : 양평근
	 * 
	 * 1. /selectSchedule (GET)
	 *  -> 사용자에게 입력폼 제공
	 *  
	 * 2. /selectFlight (POST)
	 *  -> 출발/복귀 항공편 목록 조회 + 남은 좌석 수 확인
	 *  -> 모델 및 세션에 저장 후 항공편 선택 화면 렌더링
	 *  
	 * 3. /selectSeat (POST)
	 *  -> 선택한 항공편 ID와 좌석 등급으로 좌석 배치도 조회
	 *  -> 좌석 선택 화면으로 이동
	 * */
	@Autowired
	private ObjectMapper objectMapper;  // Jackson 라이브러리의 객체 -> Java 객체 ↔ JSON 변환에 사용됨. 예) DTO 직렬화/역직렬화 등.

	private final ScheduleService scheduleService;  // 비지니스 로직을 처리하는 서비스 계층 클래스, @RequiredArgsConstructor로 주입.
	
	/**
	 *  @GetMapping : HTTP GET 요청을 처리하는 어노테이션.
	 *  /ticket/selectSchedule 경로로 브라우저가 접속했을 때 이 메서드가 호출.
	 *  string : 반환값은 뷰 이름, 이 이름을 기반으로 Thymeleaf 탬플릿을 렌더링함.
	 *  selectTicketOptionPage : 메서드 이름, 자유롭게 지정 가능하지만, 보통 의미 있는 이름을 사용.
	 *  Model model : Spring 에서 뷰(View)에 데이터를 전달할 때 사용하는 객체
	 *  -> model.addAttribute(...)를 사용하면 HTML에서 th:object 등으로 데이터를 참조가능
	 *  
	 *  *model.addAttribute("scheduleRequestDTO", new ScheduleRequestDTO());
	 *  -> 뷰로 전달할 데이터를 설정하는 부분
	 *  -> scheduleRequestDTO라는 이름으로, 새로운 ScheduleRequestDTO 객체를 생성해서 모델에 담음.
	 *  
	 *  Why? 왜 필요한가?
	 *  -> 사용자가 출발지, 도착지, 날짜 등을 입력할 폼이 필요함.
	 *  -> 이 DTO는 폼 바인딩에 사용됨 -> <form th:object="${scheduleRequestDTO}">처럼 HTML에서 사용.
	 *  
	 *  반인딩이란?
	 *  -> **바인딩(binding)**은 **사용자의 입력 값(예: form에서 입력한 값들)**을 Java의 객체에 자동으로 **매핑(연결)**하는 것.
	 * 
	 */
	@GetMapping("/selectSchedule")
	public String selectTicketOptionPage(Model model) {
		model.addAttribute("scheduleRequestDTO", new ScheduleRequestDTO());
		return "ticket/selectSchedule";
	}


	@PostMapping("/selectFlight")
	public String selectFlight(@ModelAttribute ScheduleRequestDTO scheduleRequestDTO, Model model, HttpSession session) {
		FlightSearchResultDTO result = scheduleService.findFlightsByCondition(scheduleRequestDTO);

		// 출발편
		result.getDepartureFlights().forEach(flight -> {
			flight.calculateDuration();
			List<FlightResponseDTO.SeatInfoDTO> seatInfoList = scheduleService.findSeatInfoByScheduleId(flight.getScheduleId());
			flight.setSeatInfoList(seatInfoList);
		});

		// 복귀편
		result.getReturnFlights().forEach(flight -> {
			flight.calculateDuration();
			List<FlightResponseDTO.SeatInfoDTO> seatInfoList = scheduleService.findSeatInfoByScheduleId(flight.getScheduleId());
			flight.setSeatInfoList(seatInfoList);
		});

		model.addAttribute("flightList", result.getDepartureFlights());
		model.addAttribute("returnFlightList", result.getReturnFlights());
		model.addAttribute("request", scheduleRequestDTO);
		model.addAttribute("seatGrades", SeatGrade.values());
		session.setAttribute("request", scheduleRequestDTO);

		return "ticket/selectFlight";
	}



	@PostMapping("/selectSeat")
	public String selectSeat(@ModelAttribute("request") ScheduleRequestDTO requestDTO,
			@RequestParam(name = "departureScheduleId", required = false) Long departureScheduleId,
			@RequestParam(name = "returnScheduleId", required = false) Long returnScheduleId,
			@RequestParam(name = "departureSeatGrade", required = false) SeatGrade departureSeatGrade,
			@RequestParam(name = "returnSeatGrade", required = false) SeatGrade returnSeatGrade,
			@RequestParam(name = "step", required = false) String step,
			Model model) throws JsonProcessingException {

		if (step == null) step = "departure";
		model.addAttribute("step", step);

		SeatMapResponseDto departureMap = null;
		SeatMapResponseDto returnMap = null;




		if ("return".equals(step)) {
			if (returnScheduleId != null) {
				returnMap = scheduleService.getSeatMapAllGrades(returnScheduleId);
				FlightResponseDTO returnFlight = scheduleService.getFlightByScheduleId(returnScheduleId);
				model.addAttribute("returnMap", returnMap);
				model.addAttribute("returnFlight", returnFlight);
				model.addAttribute("returnMapJson", returnMap.getSeatLayout());
			} else {
				model.addAttribute("returnMapJson", Collections.emptyList());
			}
		} else {
			if (departureScheduleId != null) {
				departureMap = scheduleService.getSeatMapAllGrades(departureScheduleId);
				FlightResponseDTO departureFlight = scheduleService.getFlightByScheduleId(departureScheduleId);
				model.addAttribute("departureMap", departureMap);
				model.addAttribute("departureFlight", departureFlight);
				model.addAttribute("departureMapJson", departureMap.getSeatLayout());
			} else {
				model.addAttribute("departureMapJson", Collections.emptyList());
			}
		}

		model.addAttribute("departureSeatGrade", departureSeatGrade);
		model.addAttribute("returnSeatGrade", returnSeatGrade);
		model.addAttribute("request", requestDTO);


		model.addAttribute("departureScheduleId", departureScheduleId);
		model.addAttribute("returnScheduleId", returnScheduleId);

		model.addAttribute("tripType", requestDTO.getTripType());


		System.out.println("Trip Type: " + requestDTO.getTripType());
		System.out.println("Return Schedule ID: " + returnScheduleId);
		System.out.println("departureMap: " + departureMap);
		System.out.println("returnMap: " + returnMap );

		return "ticket/selectSeat";
	}

	// GET 방식: 주로 '다음' 버튼 클릭 또는 URL 직접 접근용
	@GetMapping("/selectSeat")
	public String selectSeatGet(@RequestParam(name = "departureScheduleId", required = false) Long departureScheduleId,
			@RequestParam(name = "returnScheduleId", required = false) Long returnScheduleId,
			@RequestParam(name = "departureSeatGrade", required = false) SeatGrade departureSeatGrade,
			@RequestParam(name = "returnSeatGrade", required = false) SeatGrade returnSeatGrade,
			@RequestParam(name = "tripType", required = false) TripType tripType,
			@RequestParam(name = "passengerCount", required = false) Integer passengerCount,
			@RequestParam(name = "step", required = false) String step,
			@RequestParam(name = "departureSeatIds", required = false) List<String> departureSeatIds,
			Model model) throws JsonProcessingException {


		SeatMapResponseDto departureMap = null;
		SeatMapResponseDto returnMap = null;

		model.addAttribute("step", step);

		if ("return".equals(step)) {
			if (returnScheduleId != null) {
				returnMap = scheduleService.getSeatMapAllGrades(returnScheduleId);
				FlightResponseDTO returnFlight = scheduleService.getFlightByScheduleId(returnScheduleId);
				model.addAttribute("returnMap", returnMap);
				model.addAttribute("returnFlight", returnFlight);
				model.addAttribute("returnMapJson", returnMap.getSeatLayout());
			} else {
				model.addAttribute("returnMapJson", Collections.emptyList());
			}
		} else {
			if (departureScheduleId != null) {
				departureMap = scheduleService.getSeatMapAllGrades(departureScheduleId);
				FlightResponseDTO departureFlight = scheduleService.getFlightByScheduleId(departureScheduleId);
				model.addAttribute("departureMap", departureMap);
				model.addAttribute("departureFlight", departureFlight);
				model.addAttribute("departureMapJson", departureMap.getSeatLayout());
			} else {
				model.addAttribute("departureMapJson", Collections.emptyList());
			}
		}

		model.addAttribute("departureSeatGrade", departureSeatGrade);
		model.addAttribute("returnSeatGrade", returnSeatGrade);
		model.addAttribute("tripType", tripType);
		model.addAttribute("passengerCount", passengerCount);
		model.addAttribute("departureSeatIds", departureSeatIds != null ? departureSeatIds : List.of());

		model.addAttribute("departureScheduleId", departureScheduleId);
		model.addAttribute("returnScheduleId", returnScheduleId);

		System.out.println("요청받은 returnScheduleId: " + returnScheduleId);
		System.out.println("요청받은 returnMap: " + returnMap);
		System.out.println("요청받은 returnMap.getSeatLayout: " + new ObjectMapper().writeValueAsString(returnMap.getSeatLayout()));



		return "ticket/selectSeat";
	}

	// 좌표 예시
	@GetMapping("/coordinateSeat")
	public String exampleSeat(Model model) {
		return "ticket/coordinateSeat";
	}
}
