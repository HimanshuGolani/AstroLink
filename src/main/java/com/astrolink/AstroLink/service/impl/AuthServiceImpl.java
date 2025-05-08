package com.astrolink.AstroLink.service.impl;

import com.astrolink.AstroLink.dto.request.LoginRequestDto;
import com.astrolink.AstroLink.dto.request.RegistrationRequest;
import com.astrolink.AstroLink.dto.response.LoginResponseDto;
import com.astrolink.AstroLink.entity.PaymentStatus;
import com.astrolink.AstroLink.entity.Role;
import com.astrolink.AstroLink.entity.User;
import com.astrolink.AstroLink.exception.custom.DataNotFoundException;
import com.astrolink.AstroLink.exception.custom.EmailAlreadyExistsException;
import com.astrolink.AstroLink.repository.UserRepository;
import com.astrolink.AstroLink.service.AuthService;
import com.astrolink.AstroLink.util.FinderClassUtil;
import com.astrolink.AstroLink.util.RoleMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    @Transactional
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

            user.setPaymentStatus(PaymentStatus.PAID);

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


    @Override
    public Map<String, Object> login(LoginRequestDto loginRequest) {
        // Get the authenticated user
        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new DataNotFoundException("User not found"));

        try {

            System.out.println(user.toString());

            // Authenticate the user
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(),
                            loginRequest.getPassword()
                    )
            );


            // Create login response
            LoginResponseDto loginResponse = new LoginResponseDto(
                    user.getFirstName() + " " + user.getLastName()
            );

            // Generate JWT token
            String token = tokenService.generateToken(authentication,user.getId());

            // Create response map
            Map<String, Object> response = new HashMap<>();
            response.put("user", loginResponse);
            response.put("token", token);

            return response;
        }
        catch (BadCredentialsException e) {
            throw new BadCredentialsException("Invalid email or password");
        } catch (DisabledException e) {
            throw new DisabledException("Account is disabled");
        } catch (AuthenticationException e) {
            throw new RuntimeException("Authentication failed: " + e.getMessage());
        }
    }
}