package com.astrolink.AstroLink.service.impl;

import com.astrolink.AstroLink.exception.custom.PaymentException;
import com.astrolink.AstroLink.dto.request.PaymentRequestDto;
import com.astrolink.AstroLink.dto.response.PaymentStatusResponseDto;
import com.astrolink.AstroLink.service.StripeService;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StripeServiceImpl implements StripeService {

    @Value("${stripe.secret-key}")
    private String secretKey;

    @Value("${stripe.success-url}")
    private String successUrl;

    @Value("${stripe.cancel-url}")
    private String cancelUrl;

    @PostConstruct
    public void init() {
        Stripe.apiKey = secretKey;
    }

    public PaymentStatusResponseDto checkoutProducts(PaymentRequestDto paymentRequest) {

        try {
            SessionCreateParams.LineItem.PriceData.ProductData productData =
                    SessionCreateParams.LineItem.PriceData.ProductData.builder()
                            .setName(paymentRequest.getName())
                            .build();

            SessionCreateParams.LineItem.PriceData priceData =
                    SessionCreateParams.LineItem.PriceData.builder()
                            .setCurrency(paymentRequest.getCurrency() != null ? paymentRequest.getCurrency() : "INR")
                            .setUnitAmount(paymentRequest.getAmount())
                            .setProductData(productData)
                            .build();

            SessionCreateParams.LineItem lineItem =
                    SessionCreateParams.LineItem.builder()
                            .setPriceData(priceData)
                            .setQuantity(1L)
                            .build();

            SessionCreateParams params =
                    SessionCreateParams.builder()
                            .addLineItem(lineItem)
                            .setMode(SessionCreateParams.Mode.PAYMENT)
                            .setSuccessUrl(successUrl)
                            .setCancelUrl(cancelUrl)
                            .build();

            Session session = Session.create(params);

            return PaymentStatusResponseDto.builder()
                    .status("SUCCESS")
                    .message("Payment session created")
                    .sessionId(session.getId())
                    .sessionUrl(session.getUrl())
                    .build();

        } catch (StripeException e) {
            throw new PaymentException("Failed to create Stripe session", e);
        }
    }

    public PaymentStatusResponseDto verifyPayment(String sessionId) {

        try {
            Session session = Session.retrieve(sessionId);

            String paymentStatus = session.getPaymentStatus();
// TODO : replcae this dto and change the variable in user db. to payed
            if ("paid".equalsIgnoreCase(paymentStatus)) {
                return PaymentStatusResponseDto.builder()
                        .status("SUCCESS")
                        .message("Payment has been successfully verified")
                        .sessionId(sessionId)
                        .sessionUrl(session.getUrl())
                        .build();
            } else {
                return PaymentStatusResponseDto.builder()
                        .status("FAILED")
                        .message("Payment not completed")
                        .sessionId(sessionId)
                        .sessionUrl(session.getUrl())
                        .build();
            }

        } catch (StripeException e) {
            throw new PaymentException("Stripe session verification failed", e);
        }
    }

}
