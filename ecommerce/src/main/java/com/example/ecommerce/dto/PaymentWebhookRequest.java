package com.example.ecommerce.dto;

import lombok.Data;

@Data
public class PaymentWebhookRequest{
    private String event;
    private Payload payload;
    
    @Data
    public static class Payload{
        private PaymentInfo payment;
    }
    @Data
    public static class PaymentInfo{
        private String id;
        private String order_id;
        private String status;
    }
}