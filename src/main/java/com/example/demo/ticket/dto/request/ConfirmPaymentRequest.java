package com.example.demo.ticket.dto.request;

import java.util.List;

import lombok.Data;

@Data
public class ConfirmPaymentRequest {
	 private Long scheduleId;
	 private List<String> seatNumbers;
}
