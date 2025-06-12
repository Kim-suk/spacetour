package com.example.demo.reserve.repository;

import com.example.demo.reserve.dto.ReserveDTO;

import org.springframework.data.repository.query.Param; 

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReserveRepository extends JpaRepository<ReserveDTO, Long> {
    Optional<ReserveDTO> findByReserveDate(String reserveDate);
	int countByReserveDate(String date);
	Optional<ReserveDTO> findByReserveDateAndPlanetAndSelectedSeats(String reserveDate, String planet, String selectedSeats);
	List<ReserveDTO> findByReserveDateAndPlanet(String reserveDate, String planet);
    List<ReserveDTO> findByLoginIdOrderByReserveDateDesc(String loginId);
	List<ReserveDTO> findByLoginId(String loginId);
	void deleteByIdAndLoginId(int reserveId, String loginId);
    Optional<ReserveDTO> findByIdAndLoginId(Long id, String loginId);
	List<ReserveDTO> findByLoginIdAndStatus(String loginId, String string);
	
	@Query("SELECT r FROM ReserveDTO r WHERE r.loginId = :loginId AND r.status = 'PAID'")
	List<ReserveDTO> findPaidReservationsByLoginId(@Param("loginId") String loginId);


}
