package com.example.demo.order.service;

import java.util.List;

import com.example.demo.order.dto.OrderDTO;

public interface OrderService {
	List<OrderDTO> getOrdersByMemberId(String memberId);

}
