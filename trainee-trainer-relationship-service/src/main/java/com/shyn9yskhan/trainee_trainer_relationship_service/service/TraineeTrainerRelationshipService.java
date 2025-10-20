package com.shyn9yskhan.trainee_trainer_relationship_service.service;

import java.util.List;

public interface TraineeTrainerRelationshipService {
    void associate(String traineeId, String trainerId);
    void associateTraineeWithTrainers(String traineeId, List<String> trainerIds);
    void disassociate(String traineeId, String trainerId);
    boolean exists(String traineeId, String trainerId);
    List<String> getTrainersForTrainee(String traineeId);
    List<String> getTraineesForTrainer(String trainerId);
}
