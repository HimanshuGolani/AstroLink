package com.astrolink.AstroLink.service;

import com.astrolink.AstroLink.dto.request.RegistrationRequest;

public interface UserService {
    void register(RegistrationRequest registrationRequest);
}
