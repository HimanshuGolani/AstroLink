package com.astrolink.AstroLink.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Document(collection = "users")
@Data
public class User implements UserDetails {
    @Id
    private UUID id;
    private String firstName;
    private String lastName;
    @Indexed(unique = true)
    private String email;
    private String password;
    private String phoneNumber;
    private Role role;
    private PaymentStatus paymentStatus = PaymentStatus.PENDING;
    private String expertise;
    private double rating;

    // Account status fields
    private boolean accountNonExpired = true;
    private boolean accountNonLocked = true;
    private boolean credentialsNonExpired = true;
    private boolean enabled = true;

    private Set<UUID> consultationRequestIds = new TreeSet<>();
    private Set<UUID> blockedAstrologerIds = new HashSet<>();
    private Set<UUID> acceptedConsultationIds = new HashSet<>();
    private Set<UUID> activeChatSessionIds = new HashSet<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // This returns authorities for all roles, which is probably not what you want
        // Let's fix it to return only the user's assigned role
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}