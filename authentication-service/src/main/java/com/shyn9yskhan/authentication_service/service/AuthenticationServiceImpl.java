package com.shyn9yskhan.authentication_service.service;

import com.shyn9yskhan.authentication_service.client.TraineeServiceClient;
import com.shyn9yskhan.authentication_service.client.TrainerServiceClient;
import com.shyn9yskhan.authentication_service.client.UserServiceClient;
import com.shyn9yskhan.authentication_service.dto.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserServiceClient userServiceClient;
    private final TraineeServiceClient traineeServiceClient;
    private final TrainerServiceClient trainerServiceClient;

    public AuthenticationServiceImpl(AuthenticationManager authenticationManager, JwtService jwtService, UserServiceClient userServiceClient, TraineeServiceClient traineeServiceClient, TrainerServiceClient trainerServiceClient) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userServiceClient = userServiceClient;
        this.traineeServiceClient = traineeServiceClient;
        this.trainerServiceClient = trainerServiceClient;
    }

    @Override
    public GenerateTokenResponse generateToken(AuthenticationRequest authenticationRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationRequest.getUsername(),
                        authenticationRequest.getPassword()
                )
        );
        if (!authentication.isAuthenticated()) throw new UsernameNotFoundException("Username or password is incorrect");

        ResponseEntity<UserDto> userServiceClientResponse = userServiceClient.getUserByUsername(authenticationRequest.getUsername());
        if (userServiceClientResponse.getStatusCode() != HttpStatus.OK) throw new RuntimeException("Failed to fetch user: " + authenticationRequest.getUsername() + ", Status: " + userServiceClientResponse.getStatusCode());

        UserDto userDto = Objects.requireNonNull(userServiceClientResponse.getBody(), "User not found");
        Role role = Objects.requireNonNull(userDto.getRole(), "user role is missing");

        String secondId;
        switch (role) {
            case TRAINEE -> {
                ResponseEntity<GetTraineeIdByUserIdResponse> traineeServiceClientResponse = traineeServiceClient.getTraineeIdByUserId(userDto.getId());

                if (traineeServiceClientResponse.getStatusCode() != HttpStatus.OK) throw new RuntimeException("Failed to fetch traineeId with userId: " + userDto.getId() + ", Status: " + traineeServiceClientResponse.getStatusCode());
                GetTraineeIdByUserIdResponse responseBody = Objects.requireNonNull(traineeServiceClientResponse.getBody(), "Trainee not found");

                secondId = responseBody.traineeId();
            }
            case TRAINER -> {
                ResponseEntity<GetTrainerIdByUserIdResponse> trainerServiceClientResponse = trainerServiceClient.getTrainerIdByUserId(userDto.getId());

                if (trainerServiceClientResponse.getStatusCode() != HttpStatus.OK) throw new RuntimeException("Failed to fetch trainerId with userId: " + userDto.getId() + ", Status: " + trainerServiceClientResponse.getStatusCode());
                GetTrainerIdByUserIdResponse responseBody = Objects.requireNonNull(trainerServiceClientResponse.getBody(), "Trainer not found");

                secondId = responseBody.trainerId();
            }
            default -> throw new IllegalArgumentException("Unsupported role: " + role);
        }
        String token = jwtService.generateToken(userDto.getId(), role, secondId);
        return new GenerateTokenResponse(token);
    }

    @Override
    public void validateToken(String token) {
        jwtService.validateToken(token);
    }
}
