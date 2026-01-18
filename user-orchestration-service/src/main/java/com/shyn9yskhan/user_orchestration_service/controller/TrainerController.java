package com.shyn9yskhan.user_orchestration_service.controller;

import com.shyn9yskhan.user_orchestration_service.dto.trainer.*;
import com.shyn9yskhan.user_orchestration_service.service.UserOrchestrationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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
    public ResponseEntity<GetTrainerProfileResponse> getTrainerProfile(@RequestHeader("X-User-Id") String userId,
                                                                       @RequestHeader("X-User-Role") String role,
                                                                       @RequestHeader("X-User-Second-Id") String trainerId) {
        if (!"TRAINER".equals(role)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access Denied");
        }
        GetTrainerProfileResponse getTrainerProfileResponse = userOrchestrationService.getTrainerProfile(userId, trainerId);
        return ResponseEntity.ok().body(getTrainerProfileResponse);
    }

    @PutMapping
    public ResponseEntity<UpdateTrainerProfileResponse> updateTrainerProfile(@RequestHeader("X-User-Id") String userId,
                                                                             @RequestHeader("X-User-Role") String role,
                                                                             @RequestHeader("X-User-Second-Id") String trainerId,
                                                                             @RequestBody UpdateTrainerProfileRequest updateTrainerProfileRequest) {
        if (!"TRAINER".equals(role)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access Denied");
        }
        UpdateTrainerProfileResponse updateTrainerProfileResponse = userOrchestrationService.updateTrainerProfile(userId, trainerId, updateTrainerProfileRequest);
        return ResponseEntity.ok().body(updateTrainerProfileResponse);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteTrainer(@RequestHeader("X-User-Id") String userId,
                                              @RequestHeader("X-User-Role") String role,
                                              @RequestHeader("X-User-Second-Id") String trainerId) {
        if (!"TRAINER".equals(role)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access Denied");
        }
        userOrchestrationService.deleteTrainer(userId, trainerId);
        return ResponseEntity.noContent().build();
    }
}
