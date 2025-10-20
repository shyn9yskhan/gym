package com.shyn9yskhan.trainer_service.service;

import com.shyn9yskhan.trainer_service.dto.*;

import java.util.List;

public interface TrainerService {
    CreateTrainerResponse createTrainer(CreateTrainerRequest createTrainerRequest);
    GetTrainerResponse getTrainer(String trainerId);
    GetTrainingTypeIdByTrainerIdResponse getTrainingTypeIdByTrainerId(String trainerId);
    GetTrainerIdByUserIdResponse getTrainerIdByUserId(String userId);
    List<TrainerDto> getTrainersByIds(List<String> trainerIds);
    List<TrainerDto> getTrainersByUserIds(List<String> userIds);
    UpdateTrainerResponse updateTrainer(String trainerId, UpdateTrainerRequest updateTrainerRequest);
    void deleteTrainer(String trainerId);

    GetTrainerResponse getTrainerByUserId(String userId);
    UpdateTrainerResponse updateTrainerByUserId(String userId, UpdateTrainerRequest updateTrainerRequest);
    void deleteTrainerByUserId(String userId);
}
