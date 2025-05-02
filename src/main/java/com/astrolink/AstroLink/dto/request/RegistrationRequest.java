package com.astrolink.AstroLink.dto.request;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class RegistrationRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String phoneNumber;
    private String role;
}
