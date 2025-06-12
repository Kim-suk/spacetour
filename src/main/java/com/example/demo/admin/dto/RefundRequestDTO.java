package com.example.demo.admin.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RefundRequestDTO {
    private String orderId;
    private String refundReason;
}
