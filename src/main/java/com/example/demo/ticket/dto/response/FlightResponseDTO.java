package com.example.demo.ticket.dto.response;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import com.example.demo.ticket.repository.model.SpaceshipSeatPrice;
import com.example.demo.ticket.type.SeatGrade;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FlightResponseDTO {

    private Long scheduleId;
    private String spaceshipName;
    private Long spaceshipId;

    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;
    
    private String duration; // ✅ 추가

    private List<SeatInfoDTO> seatInfoList;
    
    public void calculateDuration() {
        if (departureTime != null && arrivalTime != null) {
            Duration d = Duration.between(departureTime, arrivalTime);
            long hours = d.toHours();
            long minutes = d.toMinutes() % 60;
            this.duration = String.format("%d시간 %d분", hours, minutes);
        }
    }

    // getter/setter 생략 (Lombok 써도 됨)
    
    @Getter
    @Setter
    public static class SeatInfoDTO {
        private SeatGrade seatGrade;
        private int price;
        private int remainingSeats;
        private int totalSeats;    // 추가
        private boolean operated;   // "Y" or "N"
        private boolean soldOut;    // true = 매진

        // getter/setter 생략
    }

    // getters and setters
}

