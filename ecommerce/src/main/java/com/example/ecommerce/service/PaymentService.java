package com.example.ecommerce.service;

import java.time.Instant;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.ecommerce.dto.PaymentRequest;
import com.example.ecommerce.model.Order;
import com.example.ecommerce.model.Payment;
import com.example.ecommerce.repository.OrderRepository;
import com.example.ecommerce.repository.PaymentRepository;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;

@Service
public class PaymentService{
    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private OrderRepository orderRepository;

    @Autowired 
    private RazorpayClient razorpayClient;
     
    public Payment createPayment(PaymentRequest request) throws RazorpayException{
        Order order = orderRepository.findById(request.getOrderId())
                       .orElseThrow(()-> new RuntimeException("Order not found!"));
        
        if(!"CREATED".equals(order.getStatus())){
            throw new RuntimeException("Payment can only be initiated for created orders");
        }

        JSONObject orderRequest = new JSONObject();
        orderRequest.put("amount", (int)(request.getAmount() * 100));
        orderRequest.put("currency", "INR");
        orderRequest.put("receipt",request.getOrderId());

        com.razorpay.Order razorpayOrder = razorpayClient.orders.create(orderRequest);
        
        Payment payment = new Payment();
        payment.setOrderId(order.getId());
        payment.setAmount(request.getAmount());
        payment.setPaymnetId(razorpayOrder.get("id"));
        payment.setStatus("PENDING");
        payment.setCreatedAt(Instant.now());

        return paymentRepository.save(payment);
    }

    public void processPaymentCallback(String orderId,String status,String externalId){
        Payment payment = paymentRepository.findByOrderId(orderId)
                 .orElseThrow(()-> new RuntimeException("Payment record not found for orderId: "+orderId));
        
        if("captured".equalsIgnoreCase(status) || "SUCCESS".equalsIgnoreCase(status)){
            payment.setStatus("SUCCESS");
        }
        else{
            payment.setStatus("FAILED");
        }
        payment.setPaymnetId(externalId);
        paymentRepository.save(payment);

        Order order = orderRepository.findById(orderId)
                .orElseThrow(()-> new RuntimeException("Order not found: "+ orderId));
        if("SUCCESS".equals(payment.getStatus())){
            order.setStatus("PAID");
        }
        else{
            order.setStatus("FAILED");
        }

        orderRepository.save(order);
    }
}