package com.example.demo.reserve.service;

import com.example.demo.reserve.dto.PaymentHistory;
import com.example.demo.reserve.dto.ReserveDTO;
import com.example.demo.reserve.repository.PaymentHistoryRepository;
import com.example.demo.reserve.repository.ReserveRepository;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReserveServiceImpl implements ReserveService {

	@Autowired
    private ReserveRepository reserveRepository;

    // 좌석 중복 여부 확인
    public boolean isSeatAvailable(String reserveDate, String planet, String selectedSeats) {
        return reserveRepository
                .findByReserveDateAndPlanetAndSelectedSeats(reserveDate, planet, selectedSeats)
                .isEmpty();
    }

    // 해당 날짜에 이미 예약된 인원 수가 20명 이상인지 확인
    public boolean isDateFull(String reserveDate, String planet) {
        List<ReserveDTO> existing = reserveRepository
                .findByReserveDateAndPlanet(reserveDate, planet);

        int totalSeats = existing.stream()
                .mapToInt(ReserveDTO::getPersonCount)
                .sum();

        return totalSeats >= 20;
    }

    public List<ReserveDTO> getReservationsByLoginId(String loginId) {
        return reserveRepository.findByLoginId(loginId);
    }

    public void cancelReservation(int reserveId, String loginId) {
        reserveRepository.deleteByIdAndLoginId(reserveId, loginId);
    }

    public List<ReserveDTO> getConfirmedReservations(String loginId) {
        return reserveRepository.findByLoginIdAndStatus(loginId, "CONFIRMED");
    }

    @Override
    @Transactional
    public void cancelReservation(Long reserveId, String loginId) {
        ReserveDTO reserve = reserveRepository.findByIdAndLoginId(reserveId, loginId)
            .orElseThrow(() -> new RuntimeException("예약 정보를 찾을 수 없습니다."));

        reserve.setStatus("CANCELLED"); // 논리적 취소
    }

    public void approveReservation(Long reserveId) {
        ReserveDTO dto = reserveRepository.findById(reserveId)
            .orElseThrow(() -> new RuntimeException("예약 정보 없음"));
        dto.setApproved(true);  // 승인 상태 true로 설정
        reserveRepository.save(dto);
    }


	@Override
	public Object getAllReservations() {
		// TODO Auto-generated method stub
		return reserveRepository.findAll();
	}

	 public boolean cancelReservation(Long id) {
	        Optional<ReserveDTO> opt = reserveRepository.findById(id);
	        if (opt.isEmpty()) return false;

	        ReserveDTO reservation = opt.get();
	        if ("CANCELLED".equals(reservation.getStatus())) {
	            // 이미 취소된 예약인 경우
	            return false;
	        }
	        reservation.setStatus("CANCELLED");
	        reserveRepository.save(reservation);
	        return true;
	    }

	 @Override
	    public List<ReserveDTO> getPaidReservationsByLoginId(String loginId) {
	        return reserveRepository.findPaidReservationsByLoginId(loginId);
	    }

	
}
