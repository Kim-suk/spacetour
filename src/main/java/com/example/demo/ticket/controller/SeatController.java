package com.example.demo.ticket.controller;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import com.example.demo.ticket.dto.request.LockSeatsRequestDTO;
import com.example.demo.ticket.service.SeatService;

@RestController
@RequestMapping("/api/seats")
@RequiredArgsConstructor
public class SeatController {

    private final SeatService seatService;

    /**
     * 좌석 임시 잠금 요청
     * @param seatId 잠글 좌석 ID
     * @return 잠금 성공 여부
     */
    
    // 단일 좌석
    @PostMapping("/{seatId}/lock")
    public ResponseEntity<String> lockSeat(@PathVariable Long seatId) {
        boolean success = seatService.lockSeat(seatId);
        if (success) {
            return ResponseEntity.ok("좌석이 임시 잠금되었습니다.");
        } else {
            return ResponseEntity.status(409).body("이미 예약 중이거나 잠긴 좌석입니다.");
        }
    }
    
    // 복수 좌석
    @PostMapping("/lock")
    public ResponseEntity<String> lockSeats(@RequestBody LockSeatsRequestDTO request) {
        Long scheduleId = request.getScheduleId();
        List<String> seatIdList = request.getSeatIdList();
        
        for (String seatCode : seatIdList) {
            boolean success = seatService.lockSeatBySeatCode(seatCode, scheduleId);
            if (!success) {
                return ResponseEntity.status(409).body("좌석 " + seatCode + " 는 이미 예약 중이거나 잠겨 있습니다.");
            }
        }
        return ResponseEntity.ok("좌석이 임시 잠금되었습니다.");
    }
    

    /**
     * 결제 성공 후 좌석 예약 확정 요청
     * @param seatId 예약 확정할 좌석 ID
     * @return 성공 메시지
     */
    @PostMapping("/{seatId}/confirm")
    public ResponseEntity<String> confirmReservation(@PathVariable Long seatId) {
        seatService.confirmSeatReservation(seatId);
        return ResponseEntity.ok("좌석 예약이 확정되었습니다.");
    }
}