package com.shyn9yskhan.authentication_service.service;

import com.shyn9yskhan.authentication_service.dto.GenerateTokenResponse;
import com.shyn9yskhan.authentication_service.dto.AuthenticationRequest;

public interface AuthenticationService {
    GenerateTokenResponse generateToken(AuthenticationRequest authenticationRequest);
    void validateToken(String token);
}
