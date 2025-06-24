package com.example.demo.ticket.repository.interfaces;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.ticket.repository.model.Reservation;

public interface ReservationRepository extends JpaRepository<Reservation, Long>{

}
