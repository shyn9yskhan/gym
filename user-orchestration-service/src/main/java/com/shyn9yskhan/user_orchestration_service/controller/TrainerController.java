package com.shyn9yskhan.user_orchestration_service.controller;

import com.shyn9yskhan.user_orchestration_service.dto.trainer.*;
import com.shyn9yskhan.user_orchestration_service.service.UserOrchestrationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/trainer")
public class TrainerController {
    private final UserOrchestrationService userOrchestrationService;

    public TrainerController(UserOrchestrationService userOrchestrationService) {
        this.userOrchestrationService = userOrchestrationService;
    }

    @PostMapping
    public ResponseEntity<CreateTrainerResponse> createTrainer(@RequestBody CreateTrainerRequest createTrainerRequest) {
        CreateTrainerResponse createTrainerResponse = userOrchestrationService.createTrainer(createTrainerRequest);
        return ResponseEntity.ok().body(createTrainerResponse);
    }

    @GetMapping
    public ResponseEntity<GetTrainerProfileResponse> getTrainerProfile(@RequestBody String username) {
        GetTrainerProfileResponse getTrainerProfileResponse = userOrchestrationService.getTrainerProfile(username);
        return ResponseEntity.ok().body(getTrainerProfileResponse);
    }

    @PutMapping
    public ResponseEntity<UpdateTrainerProfileResponse> updateTrainerProfile(@RequestBody UpdateTrainerProfileRequest updateTrainerProfileRequest) {
        UpdateTrainerProfileResponse updateTrainerProfileResponse = userOrchestrationService.updateTrainerProfile(updateTrainerProfileRequest);
        return ResponseEntity.ok().body(updateTrainerProfileResponse);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteTrainer(@RequestBody String username) {
        userOrchestrationService.deleteTrainer(username);
        return ResponseEntity.noContent().build();
    }
}
