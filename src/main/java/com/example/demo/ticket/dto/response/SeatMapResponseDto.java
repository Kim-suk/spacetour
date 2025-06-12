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
    private List<List<SeatStatusResponseDto>> seatLayout;
    
    
    public static SeatMapResponseDto from(List<Seat> seats) {
        int maxRow = 0;
        int maxCol = 0;

        Map<Integer, Map<Integer, SeatStatusResponseDto>> layoutMap = new HashMap<>();

        Pattern pattern = Pattern.compile("([A-Z]+)(\\d+)");

        for (Seat seat : seats) {
            String seatNumber = seat.getSeatNumber(); // 예: "A3", "E01", "AA10"
            Matcher matcher = pattern.matcher(seatNumber);

            if (!matcher.matches()) continue;

            String rowStr = matcher.group(1); // "A", "E", "AA"
            int col = Integer.parseInt(matcher.group(2)) - 1;

            int row = toRowIndex(rowStr); // "A" → 0, "B" → 1, "AA" → 26 등

            maxRow = Math.max(maxRow, row);
            maxCol = Math.max(maxCol, col);

            layoutMap
                .computeIfAbsent(row, r -> new HashMap<>())
                .put(col, SeatStatusResponseDto.from(seat));
        }
        List<List<SeatStatusResponseDto>> layout = new ArrayList<>();

        for (int r = 0; r <= maxRow; r++) {
            List<SeatStatusResponseDto> rowList = new ArrayList<>();
            for (int c = 0; c <= maxCol; c++) {
                rowList.add(layoutMap.getOrDefault(r, Collections.emptyMap()).getOrDefault(c, null));
            }
            layout.add(rowList);
        }
        return new SeatMapResponseDto(layout);
    }

    private static int toRowIndex(String rowStr) {
        // Excel-style column: "A" → 0, "B" → 1, ..., "Z" → 25, "AA" → 26, etc.
        int result = 0;
        for (char ch : rowStr.toCharArray()) {
            result = result * 26 + (ch - 'A' + 1);
        }
        return result - 1;
    }

}

