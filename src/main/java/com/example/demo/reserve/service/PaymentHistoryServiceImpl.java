package com.example.demo.reserve.service;

import com.example.demo.reserve.dto.PaymentHistory;
import com.example.demo.reserve.repository.PaymentHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class PaymentHistoryServiceImpl implements PaymentHistoryService {

    private final PaymentHistoryRepository repository;

    @Autowired
    public PaymentHistoryServiceImpl(PaymentHistoryRepository repository) {
        this.repository = repository;
    }
    
    public enum PaymentStatus {
        PAID, CANCELLED, REFUNDED
    }

    @Override
    public List<PaymentHistory> getHistoriesByLoginId(String loginId) {  // 메서드명 대소문자 수정
        return repository.findByLoginId(loginId);
    }

    @Override
    public PaymentHistory getHistoryByOrderId(String orderId) {
        return repository.findByOrderId(orderId)
                .orElseThrow(() -> new NoSuchElementException("결제 내역을 찾을 수 없습니다: orderId=" + orderId));
    }

    @Override
    public List<PaymentHistory> getAllHistories() {
        return repository.findAll();
    }

    @Override
    public PaymentHistory savePayment(PaymentHistory payment) {
        return repository.save(payment);
    }

    @Override
    public void updatePaymentStatus(String orderId, String status, String refundReason) {
        PaymentHistory payment = repository.findByOrderId(orderId)
                .orElseThrow(() -> new NoSuchElementException("결제 내역을 찾을 수 없습니다: orderId=" + orderId));

        if (!isValidStatus(status)) {
            throw new IllegalArgumentException("유효하지 않은 결제 상태입니다: " + status);
        }

        payment.setStatus(status);
        payment.setRefundReason(refundReason);
        repository.save(payment);
    }

    @Override
    public void refundPayment(String orderId, String refundReason) {
        PaymentHistory payment = repository.findByOrderId(orderId)
                .orElseThrow(() -> new NoSuchElementException("결제 내역을 찾을 수 없습니다: orderId=" + orderId));

        if (!"PAID".equals(payment.getStatus())) {
            throw new IllegalStateException("이미 취소 또는 환불된 결제입니다: orderId=" + orderId);
        }

        payment.setStatus("REFUNDED");
        payment.setRefundReason(refundReason);
        repository.save(payment);
    }

    private boolean isValidStatus(String status) {
        return "PAID".equals(status) || "CANCELLED".equals(status) || "REFUNDED".equals(status);
    }

    @Override
    public void savePaymentHistory(PaymentHistory payment) {
        repository.save(payment);
    }
}
