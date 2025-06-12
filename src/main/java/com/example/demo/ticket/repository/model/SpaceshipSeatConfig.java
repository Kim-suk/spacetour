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
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "SPACESHIP_SEAT_CONFIG")
@SequenceGenerator(name = "ssc_seq", sequenceName = "SSC_SEQ", allocationSize = 1)
@Getter
@Setter
public class SpaceshipSeatConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ssc_seq")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "SPACESHIP_ID")
    private Spaceship spaceship;

    @Enumerated(EnumType.STRING)
    @Column(name = "SEAT_GRADE", nullable = false, length = 20)
    private SeatGrade seatGrade;

    @Column(name = "DEFAULT_SEAT_COUNT", nullable = false)
    private Integer defaultSeatCount;
}
