package com.shyn9yskhan.user_orchestration_service.controller;

import com.shyn9yskhan.user_orchestration_service.dto.trainee_trainer_relationship.GetNotAssignedOnTraineeActiveTrainersResponse;
import com.shyn9yskhan.user_orchestration_service.dto.trainee_trainer_relationship.UpdateTraineesTrainerListRequest;
import com.shyn9yskhan.user_orchestration_service.dto.trainee_trainer_relationship.UpdateTraineesTrainerListResponse;
import com.shyn9yskhan.user_orchestration_service.service.UserOrchestrationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/trainee-trainer-relationship")
public class TraineeTrainerRelationshipController {
    private final UserOrchestrationService userOrchestrationService;

    public TraineeTrainerRelationshipController(UserOrchestrationService userOrchestrationService) {
        this.userOrchestrationService = userOrchestrationService;
    }

    @GetMapping("/trainee/{username}/available-trainers")
    public ResponseEntity<GetNotAssignedOnTraineeActiveTrainersResponse> getNotAssignedOnTraineeActiveTrainers(@PathVariable String username) {
        GetNotAssignedOnTraineeActiveTrainersResponse getNotAssignedOnTraineeActiveTrainersResponse = userOrchestrationService.getNotAssignedOnTraineeActiveTrainers(username);
        return ResponseEntity.ok(getNotAssignedOnTraineeActiveTrainersResponse);
    }

    @PutMapping("/trainee/{username}/trainers")
    public ResponseEntity<UpdateTraineesTrainerListResponse> updateTraineesTrainerList(@PathVariable String username, @RequestBody UpdateTraineesTrainerListRequest updateTraineesTrainerListRequest) {
        UpdateTraineesTrainerListResponse updateTraineesTrainerListResponse = userOrchestrationService.updateTraineesTrainerList(username, updateTraineesTrainerListRequest);
        return ResponseEntity.ok(updateTraineesTrainerListResponse);
    }
}
