package com.shyn9yskhan.training_service.service;

import com.shyn9yskhan.training_service.dto.*;

public interface TrainingService {
    CreateTrainingResponse createTraining(CreateTrainingRequest createTrainingRequest);
    GetTrainingResponse getTraining(String trainingId);
    UpdateTrainingResponse updateTraining(String trainingId, UpdateTrainingRequest updateTrainingRequest);
    void deleteTraining(String trainingId);
    GetTraineeTrainingsResponse getTrainingsByTraineeId(String traineeId);
    GetTrainerTrainingsResponse getTrainingsByTrainerId(String trainerId);
}
