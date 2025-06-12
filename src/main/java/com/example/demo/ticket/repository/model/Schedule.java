package com.example.demo.ticket.repository.model;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "SCHEDULE")
@Getter
@Setter
@SequenceGenerator(name = "schedule_seq", sequenceName = "SCHEDULE_SEQ", allocationSize = 1)
public class Schedule {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 혹은 시퀀스에 맞게 수정
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ROUTE_ID")
    private Route route;
    	
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SPACESHIP_ID")
    private Spaceship spaceship;

    @Column(name = "DEPARTURE_DATE", nullable = false)
    private LocalDate departureDate;

    @Column(name = "DEPARTURE_TIME", nullable = false, length = 8)
    private String departureTime;  // "HH24:MI:SS" 형태

    @Column(name = "ARRIVAL_DATE", nullable = false)
    private LocalDate arrivalDate;

    @Column(name = "ARRIVAL_TIME", nullable = false, length = 8)
    private String arrivalTime;    // "HH24:MI:SS" 형태
    
    // 1:N 매핑 - Schedule 하나에 여러 SeatInfo가 연결
    @OneToMany(mappedBy = "schedule", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<SeatInfo> seatInfoList;

}