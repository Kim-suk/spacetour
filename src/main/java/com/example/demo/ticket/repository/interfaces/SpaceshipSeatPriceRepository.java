package com.example.demo.ticket.repository.interfaces;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.ticket.repository.model.SpaceshipSeatPrice;

public interface SpaceshipSeatPriceRepository extends JpaRepository<SpaceshipSeatPrice, Long> {
    List<SpaceshipSeatPrice> findBySpaceshipId(Long spaceshipId);
}
