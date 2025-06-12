package com.example.demo.reserve.service;

import com.example.demo.reserve.dto.PaymentHistory;

import java.util.List;

public interface PaymentHistoryService {
    List<PaymentHistory> getHistoriesByLoginId(String loginId);  // 수정된 메서드명
    PaymentHistory getHistoryByOrderId(String orderId);
    List<PaymentHistory> getAllHistories();
    PaymentHistory savePayment(PaymentHistory payment);
    void updatePaymentStatus(String orderId, String status, String refundReason);
    void refundPayment(String orderId, String refundReason);
    void savePaymentHistory(PaymentHistory payment);  // PaymentController에서 사용됨
}


