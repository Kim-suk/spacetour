package com.example.demo.admin.service;

import com.example.demo.reserve.dto.PaymentHistory;
import com.example.demo.reserve.dto.ReserveDTO;

import java.util.List;

public interface AdminService {

    // 전체 예약 조회
    List<ReserveDTO> getAllReservations();

    // 예약 취소 처리 (예약 상태 변경)
    boolean cancelReservation(Long reservationId);

    // 결제 내역 전체 조회
    List<PaymentHistory> getAllPayments();

    // 주문 ID로 결제 내역 조회
    PaymentHistory getPaymentByOrderId(String orderId);

    // 결제 환불 처리
    boolean refundPayment(String orderId, String refundReason);
}
