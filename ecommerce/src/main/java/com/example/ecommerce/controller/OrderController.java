package com.example.ecommerce.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.ecommerce.dto.OrderResponse;
import com.example.ecommerce.service.OrderService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/orders")
public class OrderController{
    private OrderService orderService;

    @PostMapping("/")
    public ResponseEntity<OrderResponse> createOrder(@RequestBody String userId ) {
        OrderResponse response = orderService.createOrder(userId);
        return response != null ? ResponseEntity.ok(response) : ResponseEntity.badRequest().build();
    }
    
}