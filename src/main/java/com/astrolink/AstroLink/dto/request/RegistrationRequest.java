package com.astrolink.AstroLink.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class RegistrationRequest {
    @NotBlank(message = "First name cannot be empty")
    @Size(max = 50, message = "First name cannot be more than 50 characters")
    private String firstName;

    @NotBlank(message = "Last name cannot be empty")
    @Size(max = 50, message = "Last name cannot be more than 50 characters")
    private String lastName;

    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Please provide a valid email address")
    private String email;

    @NotBlank(message = "Password cannot be empty")
    @Size(min = 6, max = 12, message = "Password must be between 6 and 12 characters")
    private String password;

    @NotBlank(message = "Phone number cannot be empty")
    private String phoneNumber;

    @NotBlank(message = "Role cannot be empty")
    private String role;
}