package com.astrolink.AstroLink.controller;

import com.astrolink.AstroLink.dto.request.PaymentRequestDto;
import com.astrolink.AstroLink.dto.request.PaymentVerificationRequestDto;
import com.astrolink.AstroLink.dto.response.PaymentStatusResponseDto;
import com.astrolink.AstroLink.service.StripeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Payment", description = "Payment processing APIs")
public class PaymentController {

    private final StripeService stripeService;

    @Operation(summary = "Create payment checkout", description = "Initiates a payment checkout process")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Checkout created successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PaymentStatusResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid payment request", content = @Content),
            @ApiResponse(responseCode = "500", description = "Payment processor error", content = @Content)
    })
    @PostMapping("/checkout")
    public ResponseEntity<PaymentStatusResponseDto> createOrder(@RequestBody PaymentRequestDto paymentRequest) {
        PaymentStatusResponseDto data = stripeService.checkoutProducts(paymentRequest);
        return ResponseEntity.status(HttpStatus.OK).body(data);
    }

    @Operation(summary = "Verify payment", description = "Verifies the status of a payment using the session ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Payment verification successful"),
            @ApiResponse(responseCode = "400", description = "Invalid session ID", content = @Content),
            @ApiResponse(responseCode = "404", description = "Payment session not found", content = @Content)
    })
    @PostMapping("/verify")
    public ResponseEntity<?> verifyOrder(@RequestBody PaymentVerificationRequestDto verifyRequest) {
        return ResponseEntity.ok(stripeService.verifyPayment(verifyRequest.getSessionId()));
    }
}