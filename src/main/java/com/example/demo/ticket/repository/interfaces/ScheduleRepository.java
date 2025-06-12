package com.example.demo.ticket.repository.interfaces;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.ticket.repository.model.Schedule;


public interface ScheduleRepository extends JpaRepository<Schedule, Long>{

	  // 메서드 이름이 Schedule 엔티티 필드 구조에 맞게 작성되어야 합니다.
    List<Schedule> findByRouteDepartureAndRouteArrivalAndDepartureDate(
        String departure, String arrival, LocalDate departureDate
    );
    
    @Query("SELECT s FROM Schedule s JOIN FETCH s.spaceship WHERE s.id = :id")
    Optional<Schedule> findByIdWithSpaceship(@Param("id") Long id);
}
