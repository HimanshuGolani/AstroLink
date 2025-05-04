package com.astrolink.AstroLink.controller;

import com.astrolink.AstroLink.dto.request.ConsultationRequestCreateDto;
import com.astrolink.AstroLink.dto.response.ConsultationResponseDto;
import com.astrolink.AstroLink.service.ConsultationRequestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/request")
@RequiredArgsConstructor
@Tag(name = "Consultation Request", description = "Consultation Request management APIs")
public class ConsultationRequestController {

    private final ConsultationRequestService consultationRequestService;

    @PostMapping("/create/{userId}")
    @Operation(summary = "Create a new consultation request",
            description = "Creates a new consultation request for the specified user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Request created successfully",
                    content = @Content(schema = @Schema(implementation = ConsultationResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "400", description = "Invalid request data")
    })
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<ConsultationResponseDto> createConsultationRequest(
            @Parameter(description = "ID of the user creating the request") @PathVariable UUID userId,
            @Parameter(description = "Request details") @RequestBody ConsultationRequestCreateDto requestDto) {
        ConsultationResponseDto createdRequest = consultationRequestService.createConsultationRequest(userId, requestDto);
        return new ResponseEntity<>(createdRequest, HttpStatus.CREATED);
    }

    @PostMapping("/{requestId}/accept/{astrologerId}")
    @Operation(summary = "Accept a consultation request",
            description = "Astrologer accepts a consultation request if they're not blocked by the user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Request accepted successfully",
                    content = @Content(schema = @Schema(implementation = ConsultationResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Request or astrologer not found"),
            @ApiResponse(responseCode = "400", description = "Cannot accept request (blocked or already at capacity)")
    })
    @PreAuthorize("hasAuthority('ASTROLOGER')")
    public ResponseEntity<ConsultationResponseDto> acceptConsultationRequest(
            @Parameter(description = "ID of the request to accept") @PathVariable UUID requestId,
            @Parameter(description = "ID of the astrologer accepting the request") @PathVariable UUID astrologerId) {
        ConsultationResponseDto acceptedRequest = consultationRequestService.acceptConsultationRequest(requestId, astrologerId);
        return ResponseEntity.ok(acceptedRequest);
    }

    @GetMapping("/available")
    @Operation(summary = "Get all available consultation requests",
            description = "Retrieves all consultation requests that are still open for astrologers to accept")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved available requests",
            content = @Content(schema = @Schema(implementation = ConsultationResponseDto.class)))
    @PreAuthorize("hasAuthority('ASTROLOGER')")
    public ResponseEntity<List<ConsultationResponseDto>> getAllAvailableRequests() {
        List<ConsultationResponseDto> availableRequests = consultationRequestService.getAllAvailableRequests();
        return ResponseEntity.ok(availableRequests);
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Get user's consultation requests",
            description = "Retrieves all consultation requests created by a specific user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved user's requests",
                    content = @Content(schema = @Schema(implementation = ConsultationResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<List<ConsultationResponseDto>> getUserRequests(
            @Parameter(description = "ID of the user") @PathVariable UUID userId) {
        List<ConsultationResponseDto> userRequests = consultationRequestService.getUserRequests(userId);
        return ResponseEntity.ok(userRequests);
    }

    @DeleteMapping("/{requestId}")
    @Operation(summary = "Close a consultation request",
            description = "Closes a consultation request and cleans up related chat sessions")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Request closed successfully"),
            @ApiResponse(responseCode = "404", description = "Request not found")
    })
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<Void> closeRequest(
            @Parameter(description = "ID of the request to close") @PathVariable UUID requestId) {
        consultationRequestService.closeRequest(requestId);
        return ResponseEntity.noContent().build();
    }
}