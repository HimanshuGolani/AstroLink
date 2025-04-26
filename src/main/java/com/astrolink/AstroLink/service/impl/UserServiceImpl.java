package com.astrolink.AstroLink.service.impl;

import com.astrolink.AstroLink.dto.request.RegistrationRequest;
import com.astrolink.AstroLink.repository.UserRepository;
import com.astrolink.AstroLink.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public void register(RegistrationRequest registrationRequest) {

    }
}
