package com.astrolink.AstroLink.service.impl;

import com.astrolink.AstroLink.entity.ConsultationRequest;
import com.astrolink.AstroLink.entity.PaymentStatus;
import com.astrolink.AstroLink.exception.custom.PaymentException;
import com.astrolink.AstroLink.dto.request.PaymentRequestDto;
import com.astrolink.AstroLink.dto.response.PaymentStatusResponseDto;
import com.astrolink.AstroLink.repository.ConsultationRequestRepository;
import com.astrolink.AstroLink.service.StripeService;
import com.astrolink.AstroLink.util.FinderClassUtil;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StripeServiceImpl implements StripeService {

    private final FinderClassUtil finderClassUtil;
    private final ConsultationRequestRepository consultationRequestRepository;

    @Value("${stripe.secretKey}")
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

    public PaymentStatusResponseDto verifyPayment(String sessionId, UUID consultationId) {

        try {
            Session session = Session.retrieve(sessionId);

            String paymentStatus = session.getPaymentStatus();
            ConsultationRequest consultationRequest = finderClassUtil.findConsultationRequestById(consultationId);
            if ("paid".equalsIgnoreCase(paymentStatus)) {
                consultationRequest.setPaymentStatus(PaymentStatus.PAID);
                consultationRequestRepository.save(consultationRequest);
                return PaymentStatusResponseDto.builder()
                        .status("SUCCESS")
                        .message("Payment has been successfully verified")
                        .sessionId(sessionId)
                        .sessionUrl(session.getUrl())
                        .build();
            } else {
                consultationRequest.setPaymentStatus(PaymentStatus.FAILED);
                consultationRequestRepository.save(consultationRequest);
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
