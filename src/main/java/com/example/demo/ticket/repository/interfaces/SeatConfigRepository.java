package com.example.demo.ticket.repository.interfaces;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.ticket.repository.model.SpaceshipSeatConfig;

public interface SeatConfigRepository extends JpaRepository<SpaceshipSeatConfig, Long>{

	List<SpaceshipSeatConfig> findBySpaceshipId(Long id);

}
