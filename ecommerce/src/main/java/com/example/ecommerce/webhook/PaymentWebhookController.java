package com.example.ecommerce.webhook;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.ecommerce.dto.PaymentWebhookRequest;
import com.example.ecommerce.service.PaymentService;

@RestController
@RequestMapping("/api/webhooks")
public class PaymentWebhookController{
    @Autowired
    private PaymentService paymentService;

    @PostMapping("/payment")
    public ResponseEntity<String> handlePaymentWebhook(@RequestBody PaymentWebhookRequest webhookrequest) {
        String externalId = webhookrequest.getPayload().getPayment().getId();
        String razorpayOrderId= webhookrequest.getPayload().getPayment().getOrder_id();
        String status = webhookrequest.getPayload().getPayment().getStatus();

        paymentService.processPaymentCallback(razorpayOrderId,status,externalId);
        return ResponseEntity.ok("Webhook processed successfully!");
    }
    
}