package com.shyn9yskhan.training_orchestration_service.service;

import com.shyn9yskhan.training_orchestration_service.dto.*;

public interface TrainingOrchestrationService {
    GetTraineeTrainingsListResponse getTraineeTrainingsList(GetTraineeTrainingsListRequest getTraineeTrainingsListRequest);
    GetTrainerTrainingsListResponse getTrainerTrainingsList(GetTrainerTrainingsListRequest getTrainerTrainingsListRequest);
    void addTraining(AddTrainingRequest addTrainingRequest);
}
