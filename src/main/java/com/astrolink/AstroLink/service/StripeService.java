package com.astrolink.AstroLink.service;

import com.astrolink.AstroLink.dto.request.PaymentRequestDto;
import com.astrolink.AstroLink.dto.response.PaymentStatusResponseDto;

public interface StripeService {
    PaymentStatusResponseDto checkoutProducts(PaymentRequestDto paymentRequest);
    PaymentStatusResponseDto verifyPayment(String sessionId);
}
