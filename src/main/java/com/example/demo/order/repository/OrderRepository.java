package com.example.demo.order.repository;

import com.example.demo.order.dto.OrderDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OrderRepository extends JpaRepository<OrderDTO, Long> {
    List<OrderDTO> findByMemberId(String memberId);
}
