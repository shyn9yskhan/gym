package com.shyn9yskhan.trainee_service.service;

import com.shyn9yskhan.trainee_service.dto.*;

import java.util.List;

public interface TraineeService {
    CreateTraineeResponse createTrainee(CreateTraineeRequest createTraineeRequest);
    GetTraineeResponse getTrainee(String traineeId);
    GetTraineeIdByUserIdResponse getTraineeIdByUserId(String userId);
    List<TraineeDto> getTraineesByIds(List<String> traineeIds);
    UpdateTraineeResponse updateTrainee(String traineeId, UpdateTraineeRequest updateTraineeRequest);
    void deleteTrainee(String traineeId);

    GetTraineeByUserIdResponse getTraineeByUserId(String userId);
    UpdateTraineeByUserIdResponse updateTraineeByUserId(String userId, UpdateTraineeRequest updateTraineeRequest);
    void deleteTraineeByUserId(String userId);
}
