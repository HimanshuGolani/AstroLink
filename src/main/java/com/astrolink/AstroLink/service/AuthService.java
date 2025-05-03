package com.astrolink.AstroLink.service;

import com.astrolink.AstroLink.dto.request.LoginRequestDto;
import com.astrolink.AstroLink.dto.request.RegistrationRequest;

import java.util.Map;

public interface AuthService {
    void register(RegistrationRequest registrationRequest);
    Map<String, Object> login(LoginRequestDto loginRequest);
}