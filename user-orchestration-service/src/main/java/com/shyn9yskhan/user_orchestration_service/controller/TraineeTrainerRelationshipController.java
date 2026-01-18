package com.shyn9yskhan.user_orchestration_service.controller;

import com.shyn9yskhan.user_orchestration_service.dto.trainee_trainer_relationship.GetNotAssignedOnTraineeActiveTrainersResponse;
import com.shyn9yskhan.user_orchestration_service.dto.trainee_trainer_relationship.UpdateTraineesTrainerListRequest;
import com.shyn9yskhan.user_orchestration_service.dto.trainee_trainer_relationship.UpdateTraineesTrainerListResponse;
import com.shyn9yskhan.user_orchestration_service.service.UserOrchestrationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/trainee-trainer-relationship")
public class TraineeTrainerRelationshipController {
    private final UserOrchestrationService userOrchestrationService;

    public TraineeTrainerRelationshipController(UserOrchestrationService userOrchestrationService) {
        this.userOrchestrationService = userOrchestrationService;
    }

    @GetMapping("/trainee/available-trainers")
    public ResponseEntity<GetNotAssignedOnTraineeActiveTrainersResponse> getNotAssignedOnTraineeActiveTrainers(@RequestHeader("X-User-Id") String userId,
                                                                                                               @RequestHeader("X-User-Role") String role,
                                                                                                               @RequestHeader("X-User-Second-Id") String traineeId) {
        if (!"TRAINEE".equals(role)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access Denied");
        }
        GetNotAssignedOnTraineeActiveTrainersResponse getNotAssignedOnTraineeActiveTrainersResponse = userOrchestrationService.getNotAssignedOnTraineeActiveTrainers(userId, traineeId);
        return ResponseEntity.ok(getNotAssignedOnTraineeActiveTrainersResponse);
    }

    @PutMapping("/trainee/trainers")
    public ResponseEntity<UpdateTraineesTrainerListResponse> updateTraineesTrainerList(@RequestHeader("X-User-Role") String role,
                                                                                       @RequestHeader("X-User-Second-Id") String traineeId,
                                                                                       @RequestBody UpdateTraineesTrainerListRequest updateTraineesTrainerListRequest) {
        if (!"TRAINEE".equals(role)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access Denied");
        }
        UpdateTraineesTrainerListResponse updateTraineesTrainerListResponse = userOrchestrationService.updateTraineesTrainerList(traineeId, updateTraineesTrainerListRequest);
        return ResponseEntity.ok(updateTraineesTrainerListResponse);
    }
}
