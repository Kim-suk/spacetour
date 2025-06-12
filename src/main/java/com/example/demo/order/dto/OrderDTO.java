package com.example.demo.order.dto;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "orders")
public class OrderDTO {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String memberId; // 외래키처럼 회원ID 저장
    private String itemName;
    private int amount;

    @Temporal(TemporalType.TIMESTAMP)
    private Date orderDate;

    // Getter, Setter
}
