package com.astrolink.AstroLink.controller;

import com.astrolink.AstroLink.dto.response.AcceptingAstrologersRequestDto;
import com.astrolink.AstroLink.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Operation(summary = "Get all accepted consultation requests by astrologer")
    @PreAuthorize("hasAnyAuthority('USER', 'ASTROLOGER')")
    @GetMapping("/accepted-consultations/{userId}")
    public ResponseEntity<List<AcceptingAstrologersRequestDto>> getAcceptedConsultations(@PathVariable UUID userId) {
        return ResponseEntity.ok(userService.getAllPendingAcceptationRequests(userId));
    }

    @Operation(summary = "Get dashboard data for the user or astrologer")
    @PreAuthorize("hasAnyAuthority('USER', 'ASTROLOGER')")
    @GetMapping("/dashboard/{userId}")
    public ResponseEntity<Object> getDashboardData(@PathVariable UUID userId) {
        return ResponseEntity.ok(userService.getDashboardData(userId));
    }
}
