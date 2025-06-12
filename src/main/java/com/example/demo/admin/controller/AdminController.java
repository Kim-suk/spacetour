package com.example.demo.admin.controller;

import com.example.demo.admin.dto.CancelRequestDTO;
import com.example.demo.admin.dto.RefundRequestDTO;
import com.example.demo.reserve.dto.PaymentHistory;
import com.example.demo.reserve.dto.ReserveDTO;
import com.example.demo.reserve.service.PaymentHistoryService;
import com.example.demo.reserve.service.ReserveService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final ReserveService reserveService;
    private final PaymentHistoryService paymentHistoryService;

    // 1. 예약 전체 목록 조회 (JSON)
    @GetMapping("/reservations")
    public ResponseEntity<Object> getAllReservations() {
        return ResponseEntity.ok(reserveService.getAllReservations());
    }

    // 2. 예약 취소 API
    @PostMapping("/reservations/cancel")
    public ResponseEntity<String> cancelReservation(@RequestBody CancelRequestDTO request) {
        boolean canceled = reserveService.cancelReservation(request.getId());
        if (canceled) {
            return ResponseEntity.ok("예약이 취소되었습니다.");
        } else {
            return ResponseEntity.badRequest().body("예약 취소에 실패했습니다.");
        }
    }

    // 3. 결제 내역 전체 조회 (JSON)
    @GetMapping("/payments")
    public ResponseEntity<List<PaymentHistory>> getAllPayments() {
        return ResponseEntity.ok(paymentHistoryService.getAllHistories());
    }

    // 4. 주문 ID로 결제 내역 조회
    @GetMapping("/payments/{orderId}")
    public ResponseEntity<PaymentHistory> getPaymentByOrderId(@PathVariable String orderId) {
        PaymentHistory payment = paymentHistoryService.getHistoryByOrderId(orderId);
        return ResponseEntity.ok(payment);
    }

    // 5. 결제 환불 처리
    @PostMapping("/payments/refund")
    public ResponseEntity<String> refundPayment(@RequestBody RefundRequestDTO refundRequest) {
        try {
            paymentHistoryService.refundPayment(refundRequest.getOrderId(), refundRequest.getRefundReason());
            return ResponseEntity.ok("환불이 처리되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("환불 처리 실패: " + e.getMessage());
        }
    }

}
