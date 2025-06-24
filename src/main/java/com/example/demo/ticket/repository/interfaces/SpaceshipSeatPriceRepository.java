package com.example.demo.ticket.repository.interfaces;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.ticket.repository.model.SpaceshipSeatPrice;
import com.example.demo.ticket.type.SeatGrade;

public interface SpaceshipSeatPriceRepository extends JpaRepository<SpaceshipSeatPrice, Long> {
    List<SpaceshipSeatPrice> findBySpaceshipId(Long spaceshipId);
    
    
    @Query("SELECT s FROM SpaceshipSeatPrice s WHERE s.spaceship.id = :spaceshipId AND s.seatGrade = :seatGrade")
    Optional<SpaceshipSeatPrice> findBySpaceshipIdAndSeatGrade(@Param("spaceshipId") Long spaceshipId,
                                                              @Param("seatGrade") SeatGrade seatGrade);

}
