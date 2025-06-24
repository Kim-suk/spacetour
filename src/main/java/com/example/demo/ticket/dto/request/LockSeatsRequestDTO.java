package com.example.demo.ticket.dto.request;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import lombok.Data;

@Data
public class LockSeatsRequestDTO {
	private Long scheduleId;
    private String seatIds;
    
    public List<String> getSeatIdList() {
        if (seatIds == null || seatIds.isEmpty()) return Collections.emptyList();
        return Arrays.asList(seatIds.split(","));
    }
}
