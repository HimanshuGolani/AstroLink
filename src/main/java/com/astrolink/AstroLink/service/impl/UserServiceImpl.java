package com.astrolink.AstroLink.service.impl;

import com.astrolink.AstroLink.dto.request.RegistrationRequest;
import com.astrolink.AstroLink.entity.Role;
import com.astrolink.AstroLink.entity.User;
import com.astrolink.AstroLink.exception.custom.EmailAlreadyExistsException;
import com.astrolink.AstroLink.repository.UserRepository;
import com.astrolink.AstroLink.service.UserService;
import com.astrolink.AstroLink.util.RoleMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.DuplicateKeyException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void register(RegistrationRequest registrationRequest) {

        if (userRepository.existsByEmail(registrationRequest.getEmail())) {
            throw new EmailAlreadyExistsException("Email already exists, please try with another email");
        }

        try {
            User user = this.objectMapper.convertValue(registrationRequest, User.class);
            user.setId(UUID.randomUUID());

            Role role = RoleMapper.mapToRole(registrationRequest.getRole());
            user.setRole(role);

            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userRepository.save(user);

        }
        catch (IllegalArgumentException e){
            throw new IllegalArgumentException(e.getMessage());
        }
        catch (DuplicateKeyException e) {
            throw new EmailAlreadyExistsException("This email is already registered.");
        } catch (DataAccessException e) {
            throw new RuntimeException("Database access error occurred while registering the user.");
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error during registration: " + e.getMessage());
        }
    }
}
