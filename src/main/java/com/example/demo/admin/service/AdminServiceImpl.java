package com.example.demo.admin.service;

import com.example.demo.reserve.dto.PaymentHistory;
import com.example.demo.reserve.dto.ReserveDTO;

import jakarta.transaction.Transactional;

import com.example.demo.admin.repository.AdminPaymentHistoryRepository;
import com.example.demo.admin.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final AdminRepository reserveRepository;
    private final AdminPaymentHistoryRepository paymentRepository;

    @Override
    public List<ReserveDTO> getAllReservations() {
        return reserveRepository.findAll();
    }

    @Override
    @Transactional
    public boolean cancelReservation(Long reservationId) {
        return reserveRepository.findById(reservationId)
                .map(reserve -> {
                    if (!"CANCELLED".equals(reserve.getStatus())) {
                        reserve.setStatus("CANCELLED");
                        reserveRepository.save(reserve);
                        return true;
                    }
                    return false;  // 이미 취소된 예약
                })
                .orElse(false);
    }

    @Override
    public List<PaymentHistory> getAllPayments() {
        return paymentRepository.findAll();
    }

    @Override
    public PaymentHistory getPaymentByOrderId(String orderId) {
        return paymentRepository.findByOrderId(orderId)
                .orElseThrow(() -> new NoSuchElementException("결제 내역을 찾을 수 없습니다: " + orderId));
    }

    @Override
    @Transactional
    public boolean refundPayment(String orderId, String refundReason) {
        return paymentRepository.findByOrderId(orderId)
                .map(payment -> {
                    if (!"PAID".equals(payment.getStatus())) {
                        return false; // 이미 환불 또는 취소된 결제
                    }
                    payment.setStatus("REFUNDED");
                    payment.setRefundReason(refundReason);
                    paymentRepository.save(payment);
                    return true;
                })
                .orElse(false);
    }
}
