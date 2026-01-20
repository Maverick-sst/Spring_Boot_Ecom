package com.example.ecommerce.service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.ecommerce.dto.OrderResponse;
import com.example.ecommerce.model.CartItem;
import com.example.ecommerce.model.Order;
import com.example.ecommerce.model.OrderItem;
import com.example.ecommerce.repository.CartRepository;
import com.example.ecommerce.repository.OrderItemRepository;
import com.example.ecommerce.repository.OrderRepository;

@Service
public class OrderService{
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired 
    private CartRepository cartRepository;

    public OrderResponse createOrder(String userId){
        List<CartItem> cartItems = cartRepository.findByUserId(userId);
        if(!cartItems.isEmpty()){
            double totalAmount=0.0;
            for(CartItem item : cartItems){
                totalAmount += item.getPrice() * item.getQuantity();
            }
            Order order = new Order();
            order.setUserId(userId);
            order.setTotalAmount(totalAmount);
            order.setStatus("CREATED");
            order.setCreatedAt(Instant.now());
            Order savedOrder = orderRepository.save(order);

            List<OrderItem> orderItems = new ArrayList<>();
            for(CartItem item : cartItems){
                OrderItem orderItem = new OrderItem();
                orderItem.setOrderId(order.getId());
                orderItem.setPrice(item.getPrice());
                orderItem.setProductId(item.getProductId());
                orderItem.setQuantity(item.getQuantity());
                orderItems.add(orderItem);

            }
            
            cartRepository.deleteAll(cartItems);
            OrderResponse response = new OrderResponse();
            response.setCreatedAt(savedOrder.getCreatedAt());
            response.setId(savedOrder.getId());
            response.setUserId(savedOrder.getUserId());
            response.setTotalAmount(savedOrder.getTotalAmount());
            response.setItems(orderItems);
            response.setStatus(savedOrder.getStatus());

            return response;

        }
        return null;
    }
}