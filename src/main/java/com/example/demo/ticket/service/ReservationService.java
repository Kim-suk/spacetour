package com.example.demo.ticket.service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.ticket.dto.request.ConfirmReservationRequestDTO;
import com.example.demo.ticket.dto.response.ConfirmReservationResponseDTO;
import com.example.demo.ticket.repository.interfaces.FinalizedReservationRepository;
import com.example.demo.ticket.repository.interfaces.ScheduleRepository;
import com.example.demo.ticket.repository.interfaces.SeatRepository;
import com.example.demo.ticket.repository.interfaces.SpaceshipSeatPriceRepository;
import com.example.demo.ticket.repository.model.FinalizedReservation;
import com.example.demo.ticket.repository.model.Schedule;
import com.example.demo.ticket.repository.model.Seat;
import com.example.demo.ticket.repository.model.Spaceship;
import com.example.demo.ticket.type.SeatStatus;
import com.example.demo.ticket.type.TripType;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ScheduleRepository scheduleRepository;
    private final SpaceshipSeatPriceRepository seatPriceRepository;
    private final FinalizedReservationRepository finalizedReservationRepository;
    private final SeatRepository seatRepository;

    public ConfirmReservationResponseDTO prepareReservation(ConfirmReservationRequestDTO dto) {
        ConfirmReservationResponseDTO response = new ConfirmReservationResponseDTO();
        
        

        
        // 1. 출발편
        Schedule departureSchedule = scheduleRepository.findById(dto.getDepartureScheduleId())
            .orElseThrow(() -> new IllegalArgumentException("출발편 정보를 찾을 수 없습니다."));
        Spaceship departureShip = departureSchedule.getSpaceship();
        Long departureShipId = departureShip.getId();
        List<String> departureSeats = dto.getDepartureSeatIds();
        System.out.println("⚠️ [LOG] departureSeatIds in responseDto: " + dto.getDepartureSeatIds());

        
        // 출발편 가격 조회
        int departurePricePerSeat = seatPriceRepository
            .findBySpaceshipIdAndSeatGrade(departureShipId, dto.getDepartureSeatGrade())
            .orElseThrow(() -> new IllegalArgumentException("출발편 가격 정보를 찾을 수 없습니다."))
            .getBasePrice();

        int departureTotalPrice = departurePricePerSeat * departureSeats.size();

        // 출발편 LocalDateTime 변환
        LocalDateTime departureDateTime = LocalDateTime.of(
            departureSchedule.getDepartureDate(),
            LocalTime.parse(departureSchedule.getDepartureTime())
        );

        LocalDateTime arrivalDateTime = LocalDateTime.of(
            departureSchedule.getArrivalDate(),
            LocalTime.parse(departureSchedule.getArrivalTime())
        );

        // 2. 복귀편 (ROUNDTRIP일 때만)
        int returnTotalPrice = 0;
        List<String> returnSeats = new ArrayList<>();
        Schedule returnSchedule = null;
        Spaceship returnShip = null;

        LocalDateTime returnDepartureDateTime = null;
        LocalDateTime returnArrivalDateTime = null;

        if (dto.getTripType() == TripType.ROUNDTRIP && dto.getReturnScheduleId() != null) {
            returnSchedule = scheduleRepository.findById(dto.getReturnScheduleId())
                .orElseThrow(() -> new IllegalArgumentException("복귀편 정보를 찾을 수 없습니다."));
            returnShip = returnSchedule.getSpaceship();
            Long returnShipId = returnShip.getId();

            returnSeats = dto.getReturnSeatIds();
            System.out.println("⚠️ [LOG] returnSeatIds in responseDto: " + dto.getReturnSeatIds());

            int returnPricePerSeat = seatPriceRepository
                .findBySpaceshipIdAndSeatGrade(returnShipId, dto.getReturnSeatGrade())
                .orElseThrow(() -> new IllegalArgumentException("복귀편 가격 정보를 찾을 수 없습니다."))
                .getBasePrice();

            returnTotalPrice = returnPricePerSeat * returnSeats.size();

            // 복귀편 LocalDateTime 변환
            returnDepartureDateTime = LocalDateTime.of(
                returnSchedule.getDepartureDate(),
                LocalTime.parse(returnSchedule.getDepartureTime())
            );

            returnArrivalDateTime = LocalDateTime.of(
                returnSchedule.getArrivalDate(),
                LocalTime.parse(returnSchedule.getArrivalTime())
            );

            // 복귀편 정보 세팅
            response.setReturnScheduleId(dto.getReturnScheduleId());
            response.setReturnSpaceshipName(returnShip.getName());
            response.setReturnRoute(returnSchedule.getRoute().getDeparture() + " → " + returnSchedule.getRoute().getArrival());
            response.setReturnTime(returnDepartureDateTime);
            response.setReturnArrivalTime(returnArrivalDateTime);
            response.setReturnGrade(dto.getReturnSeatGrade());
            response.setReturnSeatIds(returnSeats);
        }

        // 3. 응답 세팅
        response.setTripType(dto.getTripType());
        response.setPassengerCount(dto.getPassengerCount());
        
        response.setDepartureScheduleId(dto.getDepartureScheduleId());
        response.setDepartureSpaceshipName(departureShip.getName());
        response.setDepartureRoute(departureSchedule.getRoute().getDeparture() + " → " + departureSchedule.getRoute().getArrival());
        response.setDepartureTime(departureDateTime);
        response.setArrivalTime(arrivalDateTime);
        response.setDepartureGrade(dto.getDepartureSeatGrade());
        response.setDepartureSeatIds(departureSeats);

        response.setDeparturePrice(departureTotalPrice);
        response.setReturnPrice(returnTotalPrice);
        response.setTotalPrice(departureTotalPrice + returnTotalPrice);

        return response;
    }
    
    @Transactional
    public void finalizeReservation(ConfirmReservationResponseDTO reservationDTO, String loginId, String orderId) {
        
        // 1. FinalizedReservation 엔티티 생성 및 저장
        FinalizedReservation finalized = FinalizedReservation.builder()
            .loginId(loginId)
            .orderId(orderId)
            .departureScheduleId(reservationDTO.getDepartureScheduleId())
            .returnScheduleId(reservationDTO.getReturnScheduleId())
            .departureSeatGrade(reservationDTO.getDepartureGrade())
            .returnSeatGrade(reservationDTO.getReturnGrade())
            .tripType(reservationDTO.getTripType())
            .departureSeatIds(String.join(",", reservationDTO.getDepartureSeatIds()))
            .returnSeatIds(String.join(",", reservationDTO.getReturnSeatIds()))
            .passengerCount(reservationDTO.getPassengerCount())
            .totalPrice(reservationDTO.getTotalPrice())
            .reservationDate(LocalDateTime.now())
            .paymentStatus("SUCCESS")
            .build();

        finalizedReservationRepository.save(finalized);
        
     
        
     // 2. 좌석 상태 변경 - departure 좌석
        List<String> depSeats = reservationDTO.getDepartureSeatIds();
        System.out.println("departureSeatIds: " + reservationDTO.getDepartureSeatIds());

        for (String seatId : depSeats) {
        	// 디버깅용 로그 추가
            System.out.println("departureScheduleId: " + reservationDTO.getDepartureScheduleId());
            System.out.println("seatId: " + seatId);
            
            Seat seat = seatRepository.findBySeatNumberAndScheduleId(seatId, reservationDTO.getDepartureScheduleId())
                .orElseThrow(() -> new IllegalArgumentException("출발편 좌석을 찾을 수 없습니다: " + seatId));
            seat.setSeatStatus(SeatStatus.RESERVED);
            seatRepository.save(seat);
        }

        // 3. 좌석 상태 변경 - return 좌석 (왕복일 경우)
        if (reservationDTO.getTripType() == TripType.ROUNDTRIP) {
            List<String> retSeats = reservationDTO.getReturnSeatIds();
            for (String seatId : retSeats) {
                Seat seat = seatRepository.findBySeatNumberAndScheduleId(seatId, reservationDTO.getReturnScheduleId())
                    .orElseThrow(() -> new IllegalArgumentException("복귀편 좌석을 찾을 수 없습니다: " + seatId));
                seat.setSeatStatus(SeatStatus.RESERVED);
                seatRepository.save(seat);
            }
        }

    }
    
}
