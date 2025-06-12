/*
 * package com.example.demo.reserve.dto;
 * 
 * import jakarta.persistence.Entity; import jakarta.persistence.FetchType;
 * import jakarta.persistence.GeneratedValue; import
 * jakarta.persistence.GenerationType; import jakarta.persistence.Id; import
 * jakarta.persistence.JoinColumn; import jakarta.persistence.ManyToOne;
 * 
 * import java.time.LocalDateTime;
 * 
 * @Entity public class PlanetDTO {
 * 
 * @Id
 * 
 * @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
 * 
 * private String reserveDate; private int reserveHour; private int
 * reserveMinute; private String reserveDayOfWeek;
 * 
 * @ManyToOne(fetch = FetchType.LAZY)
 * 
 * @JoinColumn(name = "planet_id") private Planet planet; // 예약된 행성 정보
 * 
 * private LocalDateTime createdAt;
 * 
 * public void Reservation() { this.createdAt = LocalDateTime.now(); }
 * 
 * 
 * }
 */