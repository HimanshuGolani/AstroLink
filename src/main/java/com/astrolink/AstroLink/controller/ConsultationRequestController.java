package com.astrolink.AstroLink.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/request")
public class ConsultationRequestController {
    @PostMapping("/submit")
    public ResponseEntity<?> submitRequest(@RequestBody Object requestPayload) {
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }

    @GetMapping("/active")
    public ResponseEntity<?> getActiveRequests() {
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }

    @GetMapping("/open")
    public ResponseEntity<?> getOpenRequests() {
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }

    @PostMapping("/accept")
    public ResponseEntity<?> acceptRequest(@RequestBody Object acceptRequestPayload) {
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }

    @PostMapping("/close")
    public ResponseEntity<?> closeRequest(@RequestBody Object closeRequestPayload) {
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }
}
