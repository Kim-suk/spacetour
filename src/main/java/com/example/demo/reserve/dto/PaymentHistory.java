package com.example.demo.reserve.dto;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "payment_history")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "order_id", unique = true)
    private String orderId;

    @Column(nullable = false)
    private int amount;

    @Column(nullable = false, name = "customer_name")
    private String customerName;

    @Column(name = "login_id", nullable = false)
    private String loginId;  // 사용자 ID (필요 시 추가)

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @Column(nullable = false)
    private String status;  // 예: "PAID", "CANCELLED", "REFUNDED"

    @Column(name = "refund_reason")
    private String refundReason;

    @PrePersist
    public void prePersist() {
        this.timestamp = this.timestamp == null ? LocalDateTime.now() : this.timestamp;
        this.status = this.status == null ? "PAID" : this.status;
    }

	public void setPaymentDate(LocalDateTime now) {
		// TODO Auto-generated method stub
		
	}
}
