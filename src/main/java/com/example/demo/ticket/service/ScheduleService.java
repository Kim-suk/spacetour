package com.example.demo.ticket.service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;


import com.example.demo.ticket.dto.request.ScheduleRequestDTO;
import com.example.demo.ticket.dto.response.FlightResponseDTO;
import com.example.demo.ticket.dto.response.FlightResponseDTO.SeatInfoDTO;
import com.example.demo.ticket.dto.response.FlightSearchResultDTO;
import com.example.demo.ticket.dto.response.SeatMapResponseDto;
import com.example.demo.ticket.dto.response.SeatStatusResponseDto;
import com.example.demo.ticket.repository.interfaces.ScheduleRepository;
import com.example.demo.ticket.repository.interfaces.SeatConfigRepository;
import com.example.demo.ticket.repository.interfaces.SeatRepository;
import com.example.demo.ticket.repository.interfaces.SpaceshipSeatPriceRepository;
import com.example.demo.ticket.repository.model.Schedule;
import com.example.demo.ticket.repository.model.Seat;
import com.example.demo.ticket.repository.model.Spaceship;
import com.example.demo.ticket.repository.model.SpaceshipSeatConfig;
import com.example.demo.ticket.repository.model.SpaceshipSeatPrice;
import com.example.demo.ticket.type.SeatGrade;
import com.example.demo.ticket.type.SeatStatus;
import com.example.demo.ticket.type.TripType;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScheduleService {
	
	// 의존 필드
	// DB와 통신하는 JPA Repository
	// 비행 스케줄 정보를 데이터베이스에서 가져옵니다.
	private final ScheduleRepository scheduleRepository;
	private final SeatConfigRepository seatConfigRepository;
	private final SeatRepository seatRepository;
    private final SpaceshipSeatPriceRepository seatPriceRepository;

	
	// 입력 : 사용자의 검색 조건을 담은 ScheduleRequestDTO
	// 출력 : 조건에 맞는 FlightResponseDTO 리스트(출발편 + 복귀편)
    public FlightSearchResultDTO findFlightsByCondition(ScheduleRequestDTO dto) {
        List<Schedule> departureSchedules = scheduleRepository.findByRouteDepartureAndRouteArrivalAndDepartureDate(
                dto.getDeparture(),
                dto.getDestination(),
                dto.getDepartureDate());

        List<Schedule> returnSchedules = new ArrayList<>();
        if (dto.getTripType() == TripType.ROUNDTRIP && dto.getReturnDate() != null) {
            returnSchedules = scheduleRepository.findByRouteDepartureAndRouteArrivalAndDepartureDate(
                    dto.getDestination(),
                    dto.getDeparture(),
                    dto.getReturnDate());
        }

        List<FlightResponseDTO> departureFlights = departureSchedules.stream()
                .map(this::convertToDTO)
                .toList();

        List<FlightResponseDTO> returnFlights = returnSchedules.stream()
                .map(this::convertToDTO)
                .toList();

        FlightSearchResultDTO result = new FlightSearchResultDTO();
        result.setDepartureFlights(departureFlights);
        result.setReturnFlights(returnFlights);

        return result;
    }


    // 이 convertToDTO(Schedule schedule) 메서드는 데이터베이스에서 가져온 Schedule 엔티티 객체를
    // **사용자에게 보여줄 형식(응답 DTO)**인 FlightResponseDTO로 변환해주는 역할을 합니다.
    // Schedule 객체 -> FlightResponseDTO 객체로 변환
    // 출발/도착 시간, 우주선 정보, 좌석 정보 등을 변환하여 담음
    	private FlightResponseDTO convertToDTO(Schedule schedule) {
        FlightResponseDTO dto = new FlightResponseDTO();

        dto.setScheduleId(schedule.getId());
        dto.setSpaceshipId(schedule.getSpaceship().getId());
        dto.setSpaceshipName(schedule.getSpaceship().getName());

        LocalTime departureLocalTime = LocalTime.parse(schedule.getDepartureTime());
        LocalTime arrivalLocalTime = LocalTime.parse(schedule.getArrivalTime());

        LocalDateTime departureDateTime = LocalDateTime.of(schedule.getDepartureDate(), departureLocalTime);
        LocalDateTime arrivalDateTime = LocalDateTime.of(schedule.getArrivalDate(), arrivalLocalTime);

        dto.setDepartureTime(departureDateTime);
        dto.setArrivalTime(arrivalDateTime);
        dto.calculateDuration();

        // 1. 전체 좌석 수 정보 로딩 (키: SeatGrade enum)
        List<SpaceshipSeatConfig> seatConfigs = seatConfigRepository.findBySpaceshipId(schedule.getSpaceship().getId());
        Map<SeatGrade, Integer> seatCountMap = seatConfigs.stream()
            .collect(Collectors.toMap(
                SpaceshipSeatConfig::getSeatGrade,       // enum 타입 그대로 사용
                SpaceshipSeatConfig::getDefaultSeatCount
            ));

        // 2. 좌석별 기본 가격 정보 로딩 (키: SeatGrade enum)
        List<SpaceshipSeatPrice> basePrices = schedule.getSpaceship().getSeatPrices();
        Map<SeatGrade, Integer> basePriceMap = basePrices.stream()
            .collect(Collectors.toMap(
                SpaceshipSeatPrice::getSeatGrade,        // enum 타입 그대로 사용
                SpaceshipSeatPrice::getBasePrice
            ));

        // 3. 좌석정보 변환
        List<FlightResponseDTO.SeatInfoDTO> seatInfoList = schedule.getSeatInfoList().stream()
            .map(seat -> {
                FlightResponseDTO.SeatInfoDTO seatDto = new FlightResponseDTO.SeatInfoDTO();

                seatDto.setSeatGrade(seat.getSeatGrade());  // SeatGrade enum

                // 좌석 가격 우선순위 : SeatInfo에 price 있으면 그걸 쓰고, 없으면 기본 가격 사용
                int price = seat.getPrice() != null ? seat.getPrice() : basePriceMap.getOrDefault(seat.getSeatGrade(), 0);
                seatDto.setPrice(price);

                seatDto.setRemainingSeats(seat.getRemainingSeats());
                seatDto.setOperated(seat.isOperated());
                seatDto.setSoldOut(seat.getRemainingSeats() <= 0);

                // 총 좌석 수
                seatDto.setTotalSeats(seatCountMap.getOrDefault(seat.getSeatGrade(), 0));

                return seatDto;
            })
            .collect(Collectors.toList());
        dto.setSeatInfoList(seatInfoList);
        return dto;
    }
    	
    	/**
         * 해당 스케줄의 모든 등급 좌석 정보를 조회하여 좌석 맵 반환
         */
        public SeatMapResponseDto getSeatMapAllGrades(Long scheduleId) {
            List<Seat> seats = seatRepository.findByScheduleId(scheduleId);
            System.out.println("찾은 좌석 개수: " + (seats != null ? seats.size() : "null"));
            return SeatMapResponseDto.from(seats);
        }

        /**
         * 스케줄 기반 운항 정보 및 등급별 좌석 정보 반환
         */
        public FlightResponseDTO getFlightByScheduleId(Long scheduleId) {
            Schedule schedule = scheduleRepository.findByIdWithSpaceship(scheduleId)
                    .orElseThrow(() -> new IllegalArgumentException("해당 스케줄이 존재하지 않습니다: " + scheduleId));

            Spaceship spaceship = schedule.getSpaceship();
            List<SpaceshipSeatPrice> priceList = seatPriceRepository.findBySpaceshipId(spaceship.getId());

            List<Seat> allSeats = seatRepository.findByScheduleId(scheduleId);

            List<SeatInfoDTO> seatInfoList = priceList.stream().map(price -> {
                SeatGrade grade = price.getSeatGrade();
                List<Seat> seatsOfGrade = allSeats.stream()
                        .filter(s -> s.getSeatGrade() == grade)
                        .collect(Collectors.toList());

                int totalSeats = seatsOfGrade.size();
                int reservedCount = (int) seatsOfGrade.stream()
                		.filter(s -> s.getSeatStatus() == SeatStatus.RESERVED|| (s.getSeatStatus() == SeatStatus.LOCKED && !s.isAvailableForLock()))
                        .count();

                SeatInfoDTO dto = new SeatInfoDTO();
                dto.setSeatGrade(grade);
                dto.setPrice(price.getBasePrice());
                dto.setTotalSeats(totalSeats);
                dto.setRemainingSeats(totalSeats - reservedCount);
                dto.setOperated(totalSeats > 0);
                dto.setSoldOut(reservedCount == totalSeats);
                return dto;
            }).collect(Collectors.toList());

            FlightResponseDTO response = new FlightResponseDTO();

            LocalTime departureTime = LocalTime.parse(schedule.getDepartureTime());
            LocalTime arrivalTime = LocalTime.parse(schedule.getArrivalTime());

            LocalDateTime departureDateTime = LocalDateTime.of(schedule.getDepartureDate(), departureTime);
            LocalDateTime arrivalDateTime = LocalDateTime.of(schedule.getArrivalDate(), arrivalTime);

            response.setScheduleId(schedule.getId());
            response.setSpaceshipId(spaceship.getId());
            response.setSpaceshipName(spaceship.getName());
            response.setDepartureTime(departureDateTime);
            response.setArrivalTime(arrivalDateTime);
            response.setSeatInfoList(seatInfoList);
            response.calculateDuration(); // 비행 시간 계산

            return response;
        }

        public List<FlightResponseDTO.SeatInfoDTO> findSeatInfoByScheduleId(Long scheduleId) {
            Spaceship spaceship = scheduleRepository.findByIdWithSpaceship(scheduleId)
                    .orElseThrow(() -> new IllegalArgumentException("해당 스케줄이 존재하지 않습니다: " + scheduleId))
                    .getSpaceship();

            List<SpaceshipSeatPrice> priceList = seatPriceRepository.findBySpaceshipId(spaceship.getId());
            List<Seat> allSeats = seatRepository.findByScheduleId(scheduleId);

            return priceList.stream().map(price -> {
                SeatGrade grade = price.getSeatGrade();
                List<Seat> seatsOfGrade = allSeats.stream()
                        .filter(s -> s.getSeatGrade() == grade)
                        .collect(Collectors.toList());

                int totalSeats = seatsOfGrade.size();
                int reservedCount = (int) seatsOfGrade.stream()
                		.filter(s -> s.getSeatStatus() == SeatStatus.RESERVED|| (s.getSeatStatus() == SeatStatus.LOCKED && !s.isAvailableForLock()))
                        .count();

                FlightResponseDTO.SeatInfoDTO dto = new FlightResponseDTO.SeatInfoDTO();
                dto.setSeatGrade(grade);
                dto.setPrice(price.getBasePrice());
                dto.setTotalSeats(totalSeats);
                dto.setRemainingSeats(totalSeats - reservedCount);
                dto.setOperated(totalSeats > 0);
                dto.setSoldOut(reservedCount == totalSeats);
                return dto;
            }).collect(Collectors.toList());
        }


}