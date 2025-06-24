package com.example.demo.ticket.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.ticket.repository.interfaces.ReservationRepository;
import com.example.demo.ticket.repository.interfaces.SeatRepository;
import com.example.demo.ticket.repository.model.Reservation;
import com.example.demo.ticket.repository.model.Seat;
import com.example.demo.ticket.type.SeatStatus;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SeatService {

    private final SeatRepository seatRepository;
    private final ReservationRepository reservationRepository;
    
    private static final long LOCK_TIMEOUT_MINUTES = 5;

    /**
     * 좌석 임시 잠금 처리 (결제 대기)
     */
    @Transactional
    public boolean lockSeat(Long seatId) {
        Seat seat = seatRepository.findById(seatId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 좌석입니다."));
        
        if (!seat.isAvailableForLock()) {
            return false;  // 이미 예약 중이거나 잠금 중인 좌석
        }
        
        seat.setSeatStatus(SeatStatus.LOCKED);
        seat.setLockTimestamp(LocalDateTime.now());
        seatRepository.save(seat);
        return true;
    }
    
    /**
     * 좌석 예약 확정 (결제 성공 시 호출)
     */
    @Transactional
    public void confirmSeatReservation(Long seatId) {
        Seat seat = seatRepository.findById(seatId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 좌석입니다."));
        
        // 결제 후 좌석 상태를 RESERVED로 변경하고 lockTimestamp 초기화
        seat.setSeatStatus(SeatStatus.RESERVED);
        seat.setLockTimestamp(null);
        seatRepository.save(seat);
    }
    
    /**
     * 임시 잠금 만료 좌석 해제 스케줄러 (예: 1분마다 실행)
     * 잠금 시간이 5분을 초과한 좌석은 다시 AVAILABLE로 변경
     */
    @Scheduled(fixedDelay = 60000)  // 60초마다 실행
    @Transactional
    public void releaseExpiredLocks() {
        LocalDateTime now = LocalDateTime.now();
        List<Seat> lockedSeats = seatRepository.findLockedSeatsLockedBefore(SeatStatus.LOCKED, now);

        for (Seat seat : lockedSeats) {
            seat.setSeatStatus(SeatStatus.AVAILABLE);
            seat.setLockTimestamp(null); // 락 타임 초기화
        }
    }
    
    
    // 이 부분이 진짜
    /**
     * 좌석 코드와 스케줄 ID로 좌석 임시 잠금 시도
     */
    /**
     * 좌석 코드(좌석 번호) + 스케줄 ID로 좌석 임시 잠금 시도
     * @param seatCode 좌석번호 (seatNumber)
     * @param scheduleId 스케줄 ID
     * @return 잠금 성공 여부
     */
    @Transactional
    public boolean lockSeatBySeatCode(String seatCode, Long scheduleId) {
        // 좌석 조회
        Optional<Seat> seatOpt = seatRepository.findBySeatNumberAndScheduleId(seatCode, scheduleId);
        if (seatOpt.isEmpty()) {
            return false; // 좌석 없음
        }

        Seat seat = seatOpt.get();

        // 예약 가능 여부 확인 (임시 잠금 만료 포함)
        if (!seat.isAvailableForLock()) {
            return false; // 이미 예약중이거나 잠금 중
        }

        // 임시 잠금 처리
        seat.setSeatStatus(SeatStatus.LOCKED);
        seat.setLockTimestamp(LocalDateTime.now());

        seatRepository.save(seat);
        return true;
    }
}