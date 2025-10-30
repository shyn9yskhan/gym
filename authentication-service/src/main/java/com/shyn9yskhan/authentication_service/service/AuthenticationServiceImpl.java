package com.shyn9yskhan.authentication_service.service;

import com.shyn9yskhan.authentication_service.dto.GenerateTokenResponse;
import com.shyn9yskhan.authentication_service.dto.AuthenticationRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthenticationServiceImpl(AuthenticationManager authenticationManager, JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @Override
    public GenerateTokenResponse generateToken(AuthenticationRequest authenticationRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationRequest.getUsername(),
                        authenticationRequest.getPassword()
                )
        );
        if (authentication.isAuthenticated()) {
            String token = jwtService.generateToken(authenticationRequest.getUsername());
            return new GenerateTokenResponse(token);
        }
        else throw new UsernameNotFoundException("Username or password is incorrect");
    }

    @Override
    public void validateToken(String token) {
        jwtService.validateToken(token);
    }
}
