package com.shyn9yskhan.user_orchestration_service.service;

import com.shyn9yskhan.user_orchestration_service.dto.trainee.*;
import com.shyn9yskhan.user_orchestration_service.dto.trainee_trainer_relationship.GetNotAssignedOnTraineeActiveTrainersResponse;
import com.shyn9yskhan.user_orchestration_service.dto.trainee_trainer_relationship.UpdateTraineesTrainerListRequest;
import com.shyn9yskhan.user_orchestration_service.dto.trainee_trainer_relationship.UpdateTraineesTrainerListResponse;
import com.shyn9yskhan.user_orchestration_service.dto.trainer.*;

public interface UserOrchestrationService {
    CreateTraineeResponse createTrainee(CreateTraineeRequest createTraineeRequest);
    GetTraineeProfileResponse getTraineeProfile(String userId, String traineeId);
    UpdateTraineeProfileResponse updateTraineeProfile(String userId, String traineeId, UpdateTraineeProfileRequest updateTraineeProfileRequest);
    void deleteTrainee(String userId, String traineeId);

    CreateTrainerResponse createTrainer(CreateTrainerRequest createTrainerRequest);
    GetTrainerProfileResponse getTrainerProfile(String userId, String trainerId);
    UpdateTrainerProfileResponse updateTrainerProfile(String userId, String trainerId, UpdateTrainerProfileRequest updateTrainerProfileRequest);
    void deleteTrainer(String userId, String trainerId);

    GetNotAssignedOnTraineeActiveTrainersResponse getNotAssignedOnTraineeActiveTrainers(String userId, String traineeId);
    UpdateTraineesTrainerListResponse updateTraineesTrainerList(String traineeId, UpdateTraineesTrainerListRequest updateTraineesTrainerListRequest);
}
