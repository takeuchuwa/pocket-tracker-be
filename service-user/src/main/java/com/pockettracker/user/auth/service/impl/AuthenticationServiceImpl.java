package com.pockettracker.user.auth.service.impl;

import com.pockettracker.user.auth.controller.dto.request.LoginRequest;
import com.pockettracker.user.auth.controller.dto.request.SignupRequest;
import com.pockettracker.user.auth.controller.dto.response.AuthenticationResponse;
import com.pockettracker.user.exception.UserServiceException;
import com.pockettracker.user.exception.ConflictException;
import com.pockettracker.user.auth.service.AuthenticationService;
import com.pockettracker.user.entity.User;
import com.pockettracker.user.entity.UserCredentials;
import com.pockettracker.user.entity.UserInformation;
import com.pockettracker.user.entity.enums.Role;
import com.pockettracker.user.jwt.service.JwtService;
import com.pockettracker.user.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public record AuthenticationServiceImpl(JwtService jwtService,
                                        AuthenticationManager authenticationManager,
                                        UserRepository userRepository,
                                        PasswordEncoder passwordEncoder) implements AuthenticationService {
    @Override
    public AuthenticationResponse authenticate(LoginRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.email(), request.password()));
        Optional<User> user = userRepository.findUserByUserCredentialsEmail(request.email());
        if (user.isPresent()) {
            String jwtToken = jwtService.generateToken(user.get());
            return AuthenticationResponse.builder()
                    .jwtToken(jwtToken)
                    .build();
        } else {
            throw new UserServiceException();
        }
    }

    @Override
    public AuthenticationResponse signup(SignupRequest request) {
        Optional<User> optionalUser = userRepository.findUserByUserCredentialsEmail(request.email());
        optionalUser.ifPresentOrElse(user -> {
            throw new ConflictException("User with this email already exists");
        }, () -> {
            User user = User.builder()
                    .userCredentials(populateUserCredentials(request))
                    .userInformation(populateUserInformation(request))
                    .enabled(true)
                    .deleted(false)
                    .role(Role.REGISTERED_USER)
                    .build();
            userRepository.save(user);
        });

        return authenticate(LoginRequest.builder()
                .email(request.email())
                .password(request.password())
                .build());
    }

    private UserCredentials populateUserCredentials(SignupRequest request) {
        return UserCredentials.builder()
                .email(request.email())
                .encryptedPassword(passwordEncoder.encode(request.password()))
                .build();
    }

    private UserInformation populateUserInformation(SignupRequest request) {
        return UserInformation.builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .build();
    }
}
