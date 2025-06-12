package com.example.demo.ticket.repository.model;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "SPACESHIP")
@Getter
@Setter
@SequenceGenerator(name = "spaceship_seq", sequenceName = "SPACESHIP_SEQ", allocationSize = 1)
public class Spaceship {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "spaceship_seq")
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false)
    private Integer capacity;
    
    // ✅ 우주선이 가지고 있는 좌석 가격 리스트
    @OneToMany(mappedBy = "spaceship")
    private List<SpaceshipSeatPrice> seatPrices;
}

