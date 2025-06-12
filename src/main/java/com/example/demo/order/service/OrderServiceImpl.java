package com.example.demo.order.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.order.dto.OrderDTO;
import com.example.demo.order.repository.OrderRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    @Override
    public List<OrderDTO> getOrdersByMemberId(String memberId) {
        return orderRepository.findByMemberId(memberId);
    }
}
