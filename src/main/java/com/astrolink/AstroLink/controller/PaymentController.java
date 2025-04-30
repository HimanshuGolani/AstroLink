package com.astrolink.AstroLink.controller;

import com.astrolink.AstroLink.dto.request.PaymentRequestDto;
import com.astrolink.AstroLink.dto.request.PaymentVerificationRequestDto;
import com.astrolink.AstroLink.dto.response.PaymentStatusResponseDto;
import com.astrolink.AstroLink.service.StripeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final StripeService stripeService;

    @PostMapping("/checkout")
    public ResponseEntity<PaymentStatusResponseDto> createOrder(@RequestBody PaymentRequestDto paymentRequest) {
        PaymentStatusResponseDto data = stripeService.checkoutProducts(paymentRequest);
        return ResponseEntity.status(HttpStatus.OK).body(data);
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verifyOrder(@RequestBody PaymentVerificationRequestDto verifyRequest) {
        return ResponseEntity.ok(stripeService.verifyPayment(verifyRequest.getSessionId()));
    }

}