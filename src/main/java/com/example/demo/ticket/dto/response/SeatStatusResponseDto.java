package com.example.demo.ticket.dto.response;

import com.example.demo.ticket.repository.model.Seat;
import com.example.demo.ticket.type.SeatGrade;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SeatStatusResponseDto {
    private String seatNumber;
    private boolean isReserved;
    private boolean isBlocked;
    private SeatGrade grade;
    private boolean isSelectable;

    // allowedGrade가 있을 때, 해당 등급만 선택 가능
    public static SeatStatusResponseDto from(Seat seat, SeatGrade allowedGrade) {
        boolean reserved = "Y".equals(seat.getIsReserved());
        boolean blocked = "Y".equals(seat.getIsBlocked());
        boolean selectable = !reserved && !blocked && seat.getSeatGrade() == allowedGrade;

        return new SeatStatusResponseDto(
            seat.getSeatNumber(),
            reserved,
            blocked,
            seat.getSeatGrade(),
            selectable
        );
    }

    // allowedGrade 없이 호출 시, selectable은 reserved, blocked 여부만 보고 판단
    public static SeatStatusResponseDto from(Seat seat) {
        boolean reserved = "Y".equals(seat.getIsReserved());
        boolean blocked = "Y".equals(seat.getIsBlocked());
        boolean selectable = !reserved && !blocked; // 등급 체크 없이 예약 가능 여부만 판단
        
        return new SeatStatusResponseDto(
            seat.getSeatNumber(),
            reserved,
            blocked,
            seat.getSeatGrade(),
            selectable
        );
    }
}
