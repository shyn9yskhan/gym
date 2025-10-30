package com.shyn9yskhan.authentication_service.controller;

import com.shyn9yskhan.authentication_service.dto.GenerateTokenResponse;
import com.shyn9yskhan.authentication_service.dto.AuthenticationRequest;
import com.shyn9yskhan.authentication_service.service.AuthenticationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/authentication")
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/token")
    public ResponseEntity<GenerateTokenResponse> generateToken(@RequestBody AuthenticationRequest authenticationRequest) {
        GenerateTokenResponse generateTokenResponse = authenticationService.generateToken(authenticationRequest);
        return ResponseEntity.ok(generateTokenResponse);
    }

    @GetMapping("/validate")
    public ResponseEntity<String> validateToken(@RequestParam String token) {
        authenticationService.validateToken(token);
        return ResponseEntity.ok("Token is valid");
    }
}
