package com.astrolink.AstroLink.controller;

import com.astrolink.AstroLink.dto.request.RegistrationRequest;
import com.astrolink.AstroLink.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegistrationRequest registrationRequest){
        userService.register(registrationRequest);
        return new ResponseEntity<>("User created successfully", HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<?> userLogin(){
        return null;
    }

    @PostMapping("/payment/verify")
    public ResponseEntity<?> verifyPayment(){
        return null;
    }

    @GetMapping("/profile")
    public ResponseEntity<?> findUserProfile(){
        return null;
    }

}

/*

Method	Endpoint	Description
POST	/api/auth/register	Register a Client or Astrologer (role-based)
POST	/api/auth/login	Login endpoint
POST	/api/auth/payment/verify	Verify ₹500 payment after client registration
GET	/api/auth/profile	Get logged-in user profile (client or astrologer)

*/