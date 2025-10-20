package com.shyn9yskhan.user_orchestration_service.service;

import com.shyn9yskhan.user_orchestration_service.dto.trainee.*;
import com.shyn9yskhan.user_orchestration_service.dto.trainee_trainer_relationship.GetNotAssignedOnTraineeActiveTrainersResponse;
import com.shyn9yskhan.user_orchestration_service.dto.trainee_trainer_relationship.UpdateTraineesTrainerListRequest;
import com.shyn9yskhan.user_orchestration_service.dto.trainee_trainer_relationship.UpdateTraineesTrainerListResponse;
import com.shyn9yskhan.user_orchestration_service.dto.trainer.*;

public interface UserOrchestrationService {
    CreateTraineeResponse createTrainee(CreateTraineeRequest createTraineeRequest);
    GetTraineeProfileResponse getTraineeProfile(String username);
    UpdateTraineeProfileResponse updateTraineeProfile(UpdateTraineeProfileRequest updateTraineeProfileRequest);
    void deleteTrainee(String username);

    CreateTrainerResponse createTrainer(CreateTrainerRequest createTrainerRequest);
    GetTrainerProfileResponse getTrainerProfile(String username);
    UpdateTrainerProfileResponse updateTrainerProfile(UpdateTrainerProfileRequest updateTrainerProfileRequest);
    void deleteTrainer(String username);

    GetNotAssignedOnTraineeActiveTrainersResponse getNotAssignedOnTraineeActiveTrainers(String username);
    UpdateTraineesTrainerListResponse updateTraineesTrainerList(String username, UpdateTraineesTrainerListRequest updateTraineesTrainerListRequest);
}
