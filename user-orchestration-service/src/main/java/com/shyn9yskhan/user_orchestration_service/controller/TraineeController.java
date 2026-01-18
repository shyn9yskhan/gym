package com.shyn9yskhan.user_orchestration_service.controller;

import com.shyn9yskhan.user_orchestration_service.dto.trainee.*;
import com.shyn9yskhan.user_orchestration_service.service.UserOrchestrationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/trainee")
public class TraineeController {
    private final UserOrchestrationService userOrchestrationService;

    public TraineeController(UserOrchestrationService userOrchestrationService) {
        this.userOrchestrationService = userOrchestrationService;
    }

    @PostMapping
    public ResponseEntity<CreateTraineeResponse> createTrainee(@RequestBody CreateTraineeRequest createTraineeRequest) {
        CreateTraineeResponse createTraineeResponse = userOrchestrationService.createTrainee(createTraineeRequest);
        return ResponseEntity.ok(createTraineeResponse);
    }

    @GetMapping
    public ResponseEntity<GetTraineeProfileResponse> getTraineeProfile(@RequestHeader("X-User-Id") String userId,
                                                                       @RequestHeader("X-User-Role") String role,
                                                                       @RequestHeader("X-User-Second-Id") String traineeId) {
        if (!"TRAINEE".equals(role)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access Denied");
        }
        GetTraineeProfileResponse getTraineeProfileResponse = userOrchestrationService.getTraineeProfile(userId, traineeId);
        return ResponseEntity.ok(getTraineeProfileResponse);
    }

    @PutMapping
    public ResponseEntity<UpdateTraineeProfileResponse> updateTraineeProfile(@RequestHeader("X-User-Id") String userId,
                                                                             @RequestHeader("X-User-Role") String role,
                                                                             @RequestHeader("X-User-Second-Id") String traineeId,
                                                                             @RequestBody UpdateTraineeProfileRequest updateTraineeProfileRequest) {
        if (!"TRAINEE".equals(role)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access Denied");
        }
        UpdateTraineeProfileResponse updateTraineeProfileResponse = userOrchestrationService.updateTraineeProfile(userId, traineeId, updateTraineeProfileRequest);
        return ResponseEntity.ok(updateTraineeProfileResponse);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteTrainee(@RequestHeader("X-User-Id") String userId,
                                              @RequestHeader("X-User-Role") String role,
                                              @RequestHeader("X-User-Second-Id") String traineeId) {
        if (!"TRAINEE".equals(role)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access Denied");
        }
        userOrchestrationService.deleteTrainee(userId, traineeId);
        return ResponseEntity.noContent().build();
    }
}
