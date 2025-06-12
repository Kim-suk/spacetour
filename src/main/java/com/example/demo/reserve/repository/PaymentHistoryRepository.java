package com.example.demo.reserve.repository;

import com.example.demo.reserve.dto.PaymentHistory;
import com.example.demo.reserve.dto.ReserveDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentHistoryRepository extends JpaRepository<PaymentHistory, Long> {
    
    Optional<PaymentHistory> findByOrderId(String orderId);
	List<PaymentHistory> findByLoginId(String loginId);
}
