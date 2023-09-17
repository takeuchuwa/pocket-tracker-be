package com.pockettracker.user.auth.service.impl;

import com.pockettracker.user.auth.controller.dto.request.LoginRequest;
import com.pockettracker.user.auth.controller.dto.request.SignupRequest;
import com.pockettracker.user.auth.controller.dto.response.AuthenticationResponse;
import com.pockettracker.user.auth.util.AuthConstants;
import com.pockettracker.user.exception.UserServiceException;
import com.pockettracker.user.exception.ConflictException;
import com.pockettracker.user.auth.service.AuthenticationService;
import com.pockettracker.user.entity.User;
import com.pockettracker.user.entity.UserCredentials;
import com.pockettracker.user.entity.UserInformation;
import com.pockettracker.user.entity.enums.Role;
import com.pockettracker.user.security.jwt.service.JwtService;
import com.pockettracker.user.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Optional;

@Service
public record AuthenticationServiceImpl(JwtService jwtService,
                                        AuthenticationManager authenticationManager,
                                        UserRepository userRepository,
                                        PasswordEncoder passwordEncoder) implements AuthenticationService {

    @Override
    public AuthenticationResponse authenticate(LoginRequest loginRequest, HttpServletRequest httpRequest) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.email(), loginRequest.password()));
        Optional<User> user = userRepository.findUserByUserCredentialsEmail(loginRequest.email());
        if (user.isPresent()) {
            if (isTokenValid(httpRequest, user.get())) {
                return AuthenticationResponse.builder()
                        .authToken(findCookie(httpRequest, AuthConstants.AUTH_TOKEN))
                        .refreshToken(findCookie(httpRequest, AuthConstants.REFRESH_TOKEN))
                        .build();
            }
            String jwtToken = jwtService.generateToken(user.get());
            String refreshToken = jwtService.generateJwtRtPair(jwtToken);
            return AuthenticationResponse.builder()
                    .authToken(jwtToken)
                    .refreshToken(refreshToken)
                    .build();
        } else {
            throw new UserServiceException();
        }
    }

    @Override
    public AuthenticationResponse signup(SignupRequest signupRequest, HttpServletRequest httpRequest) {
        Optional<User> optionalUser = userRepository.findUserByUserCredentialsEmail(signupRequest.email());
        optionalUser.ifPresentOrElse(user -> {
            throw new ConflictException("User with this email already exists");
        }, () -> {
            User user = User.builder()
                    .userCredentials(populateUserCredentials(signupRequest))
                    .userInformation(populateUserInformation(signupRequest))
                    .enabled(true)
                    .deleted(false)
                    .role(Role.REGISTERED_USER)
                    .build();
            userRepository.save(user);
        });

        return authenticate(LoginRequest.builder()
                .email(signupRequest.email())
                .password(signupRequest.password())
                .build(), httpRequest);
    }

    @Override
    public AuthenticationResponse refresh(String jwtToken, String refreshToken) {
        Optional<User> user = userRepository.findUserByUserCredentialsEmail(jwtService.extractUsername(jwtToken));
        if (user.isPresent() && jwtService.isJwtRtPairValid(jwtToken, refreshToken)) {
            String newJwtToken = jwtService.generateToken(user.get());
            String newRefreshToken = jwtService.generateJwtRtPair(newJwtToken);
            return AuthenticationResponse.builder()
                    .authToken(newJwtToken)
                    .refreshToken(newRefreshToken)
                    .build();
        } else {
            throw new UserServiceException();
        }
    }

    @Override
    public void addAuthCookies(HttpServletResponse response, AuthenticationResponse auth) {
        Cookie authToken = new Cookie(AuthConstants.AUTH_TOKEN, auth.authToken());
        Cookie refreshToken = new Cookie(AuthConstants.REFRESH_TOKEN, auth.refreshToken());
        authToken.setPath("/");
        refreshToken.setPath("/");
        response.addCookie(authToken);
        response.addCookie(refreshToken);
    }

    public boolean isTokenValid(HttpServletRequest request, User user) {
        String jwt = findCookie(request, AuthConstants.AUTH_TOKEN);
        return jwt != null
                && jwtService.isTokenValid(jwt)
                && jwtService.extractUsername(jwt).equals(user.getUserCredentials().getEmail());
    }

    public String findCookie(HttpServletRequest request, String cookieName) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            return Arrays.stream(cookies)
                    .filter(c -> c.getName().equals(cookieName))
                    .findFirst()
                    .map(Cookie::getValue)
                    .orElse(null);
        } else {
            return null;
        }
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
