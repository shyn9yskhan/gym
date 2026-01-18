package com.shyn9yskhan.training_orchestration_service.service;

import com.shyn9yskhan.training_orchestration_service.dto.*;

public interface TrainingOrchestrationService {
    GetTraineeTrainingsListResponse getTraineeTrainingsList(String traineeId, GetTraineeTrainingsListRequest getTraineeTrainingsListRequest);
    GetTrainerTrainingsListResponse getTrainerTrainingsList(String trainerId, GetTrainerTrainingsListRequest getTrainerTrainingsListRequest);
    void addTraining(AddTrainingRequest addTrainingRequest);
}
