package com.example.demo.ticket.dto.response;

import com.example.demo.ticket.repository.model.Seat;
import com.example.demo.ticket.type.SeatGrade;
import com.example.demo.ticket.type.SeatStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SeatStatusResponseDto {
	private String seatNumber;
    private SeatStatus seatStatus;  // boolean -> Enum으로 변경
    private SeatGrade grade;

    private int x; // 좌표 UI용
    private int y; // 좌표 UI용
    
    private String seatImage; // 추가된 필드
    
    public static SeatStatusResponseDto from(Seat seat, int x, int y) {
        // blocked 상태가 있다면 추가 처리 필요
        return new SeatStatusResponseDto(
            seat.getSeatNumber(),
            seat.getSeatStatus(),
            seat.getSeatGrade(),
            x,
            y,
            seat.getSeatImage()
        );
    }
}
