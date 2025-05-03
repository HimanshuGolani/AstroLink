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

}
