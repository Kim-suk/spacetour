package com.example.demo.admin.repository;

import com.example.demo.reserve.dto.PaymentHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminPaymentHistoryRepository extends JpaRepository<PaymentHistory, Long> {
    Optional<PaymentHistory> findByOrderId(String orderId);  // 환불 처리 등에서 사용
}
