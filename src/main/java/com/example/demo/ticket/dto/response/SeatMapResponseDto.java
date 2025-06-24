package com.example.demo.ticket.dto.response;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.example.demo.ticket.repository.model.Seat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SeatMapResponseDto {
	private List<SeatStatusResponseDto> seatLayout;

    public List<SeatStatusResponseDto> getSeatLayout() {
        return seatLayout;
    }

    public static SeatMapResponseDto from(List<Seat> seats) {
        List<SeatStatusResponseDto> layout = new ArrayList<>();

        for (Seat seat : seats) { 
            layout.add(SeatStatusResponseDto.from(
                seat,
                seat.getX() != null ? seat.getX() : 0,
                seat.getY() != null ? seat.getY() : 0
            ));
        }

        return new SeatMapResponseDto(layout);
    }

}

