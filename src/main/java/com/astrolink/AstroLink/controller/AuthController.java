package com.astrolink.AstroLink.controller;

import com.astrolink.AstroLink.dto.request.LoginRequestDto;
import com.astrolink.AstroLink.dto.request.RegistrationRequest;
import com.astrolink.AstroLink.service.AuthService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Authentication and user management APIs")
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "Register a new user", description = "Creates a new user account in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content),
            @ApiResponse(responseCode = "409", description = "User already exists", content = @Content)
    })
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegistrationRequest registrationRequest){
        authService.register(registrationRequest);
        return new ResponseEntity<>("User created successfully", HttpStatus.CREATED);
    }

    @Operation(summary = "User login", description = "Authenticates a user and returns a JWT token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login successful"),
            @ApiResponse(responseCode = "401", description = "Invalid credentials", content = @Content),
            @ApiResponse(responseCode = "403", description = "Account locked or disabled", content = @Content)
    })
    @PostMapping("/login")
    public ResponseEntity<?> userLogin(@Valid @RequestBody LoginRequestDto loginRequest) {
        try {
            Map<String, Object> response = authService.login(loginRequest);
            String token = (String) response.get("token");

            return ResponseEntity.ok()
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                    .body(response);
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Invalid credentials"));
        } catch (DisabledException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", "Account is disabled"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Authentication failed: " + e.getMessage()));
        }
    }

}