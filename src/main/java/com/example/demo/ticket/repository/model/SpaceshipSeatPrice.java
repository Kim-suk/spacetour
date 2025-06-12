package com.example.demo.ticket.repository.model;

import com.example.demo.ticket.type.SeatGrade;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "SPACESHIP_SEAT_PRICE")
@Getter
@Setter
public class SpaceshipSeatPrice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "SPACESHIP_ID", nullable = false)
    private Spaceship spaceship;

    @Enumerated(EnumType.STRING)
    @Column(name = "SEAT_GRADE", nullable = false)
    private SeatGrade seatGrade;

    @Column(name = "BASE_PRICE", nullable = false)
    private Integer basePrice;
}