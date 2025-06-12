package com.example.demo.reserve.dto;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "reservation",
       uniqueConstraints = {
           @UniqueConstraint(columnNames = {"reserve_date", "planet", "selected_seats"})
       })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ReserveDTO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "reserve_date", nullable = false)
    private String reserveDate;

    @Column(name = "reserve_hour", nullable = false)
    private int reserveHour;

    @Column(name = "reserve_minute", nullable = false)
    private int reserveMinute;

    @Column(name = "reserve_day_of_week", nullable = false)
    private String reserveDayOfWeek;

    @Column(name = "person_count", nullable = false)
    private int personCount;

    @Column(name = "planet", nullable = false)
    private String planet;

    @Column(name = "selected_seats", nullable = false)
    private String selectedSeats;

    @Column(name = "login_id", nullable = false)
    private String loginId;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "price", nullable = false)
    private int price;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.status = this.status == null ? "CONFIRMED" : this.status;
    }

    public void setApproved(boolean approved) {
        this.status = approved ? "APPROVED" : "PENDING";
    }
}
