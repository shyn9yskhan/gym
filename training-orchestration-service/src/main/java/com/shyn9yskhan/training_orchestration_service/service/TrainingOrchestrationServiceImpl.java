package com.shyn9yskhan.training_orchestration_service.service;

import com.shyn9yskhan.training_orchestration_service.client.TraineeServiceClient;
import com.shyn9yskhan.training_orchestration_service.client.TrainerServiceClient;
import com.shyn9yskhan.training_orchestration_service.client.TrainingServiceClient;
import com.shyn9yskhan.training_orchestration_service.client.UserServiceClient;
import com.shyn9yskhan.training_orchestration_service.client.dto.*;
import com.shyn9yskhan.training_orchestration_service.dto.*;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class TrainingOrchestrationServiceImpl implements TrainingOrchestrationService {
    private final TrainingServiceClient trainingServiceClient;
    private final UserServiceClient userServiceClient;
    private final TraineeServiceClient traineeServiceClient;
    private final TrainerServiceClient trainerServiceClient;

    public TrainingOrchestrationServiceImpl(TrainingServiceClient trainingServiceClient, UserServiceClient userServiceClient, TraineeServiceClient traineeServiceClient, TrainerServiceClient trainerServiceClient) {
        this.trainingServiceClient = trainingServiceClient;
        this.userServiceClient = userServiceClient;
        this.traineeServiceClient = traineeServiceClient;
        this.trainerServiceClient = trainerServiceClient;
    }

    @Override
    @CircuitBreaker(name = "trainingServiceClient", fallbackMethod = "getTraineeTrainingsListFallback")
    public GetTraineeTrainingsListResponse getTraineeTrainingsList(GetTraineeTrainingsListRequest request) {
        String username = request.username();
        GetUserIdByUsernameServiceResponse userIdResponse = userServiceClient.getUserIdByUsername(username).getBody();
        if (userIdResponse == null) return new GetTraineeTrainingsListResponse(Collections.emptyList());
        String userId = userIdResponse.userId();

        GetTraineeIdByUserIdServiceResponse traineeIdResponse = traineeServiceClient.getTraineeIdByUserId(userId).getBody();
        if (traineeIdResponse == null) return new GetTraineeTrainingsListResponse(Collections.emptyList());

        String traineeId = traineeIdResponse.traineeId();

        GetTraineeTrainingsServiceResponse trainingsResponse = trainingServiceClient.getTrainingsByTraineeId(traineeId).getBody();

        List<TrainingDto> trainingsList = (trainingsResponse == null || trainingsResponse.trainings() == null)
                ? Collections.emptyList()
                : trainingsResponse.trainings();

        LocalDate filterStartDate = request.periodFrom();
        LocalDate filterEndDate = request.periodTo();
        String requestedTrainerId = request.trainerId();
        String requestedTrainingTypeId = request.trainingTypeId();

        List<TrainingDto> filteredTrainings = trainingsList.stream()
                .filter(Objects::nonNull)
                .filter(training -> {
                    LocalDate trainingDate = training.date();
                    if (filterStartDate != null && trainingDate.isBefore(filterStartDate)) return false;
                    if (filterEndDate != null && trainingDate.isAfter(filterEndDate)) return false;
                    if (requestedTrainerId != null && !requestedTrainerId.equals(training.trainerId())) return false;
                    if (requestedTrainingTypeId != null && !requestedTrainingTypeId.equals(training.trainingTypeId())) return false;
                    return true;
                })
                .collect(Collectors.toList());
        return new GetTraineeTrainingsListResponse(filteredTrainings);
    }

    @Override
    @CircuitBreaker(name = "trainingServiceClient", fallbackMethod = "getTrainerTrainingsListFallback")
    public GetTrainerTrainingsListResponse getTrainerTrainingsList(GetTrainerTrainingsListRequest request) {
        String username = request.username();
        GetUserIdByUsernameServiceResponse userIdResponse = userServiceClient.getUserIdByUsername(username).getBody();
        if (userIdResponse == null) return new GetTrainerTrainingsListResponse(Collections.emptyList());
        String userId = userIdResponse.userId();

        GetTrainerIdByUserIdServiceResponse trainerIdResponse = trainerServiceClient.getTrainerIdByUserId(userId).getBody();
        if (trainerIdResponse == null) return new GetTrainerTrainingsListResponse(Collections.emptyList());
        String trainerId = trainerIdResponse.trainerId();

        GetTrainerTrainingsServiceResponse trainingsResponse = trainingServiceClient.getTrainingsByTrainerId(trainerId).getBody();

        List<TrainingDto> allTrainings = (trainingsResponse == null || trainingsResponse.trainings() == null)
                ? Collections.emptyList()
                : trainingsResponse.trainings();

        LocalDate filterStartDate = request.periodFrom();
        LocalDate filterEndDate = request.periodTo();
        String requestedTraineeId = request.traineeId();
        String requestedTrainingTypeId = request.trainingTypeId();

        List<TrainingDto> filteredTrainings = allTrainings.stream()
                .filter(Objects::nonNull)
                .filter(training -> {
                    LocalDate trainingDate = training.date();
                    if (filterStartDate != null && trainingDate.isBefore(filterStartDate)) return false;
                    if (filterEndDate != null && trainingDate.isAfter(filterEndDate)) return false;
                    if (requestedTraineeId != null && !requestedTraineeId.equals(training.traineeId())) return false;
                    if (requestedTrainingTypeId != null && !requestedTrainingTypeId.equals(training.trainingTypeId())) return false;
                    return true;
                })
                .collect(Collectors.toList());
        return new GetTrainerTrainingsListResponse(filteredTrainings);
    }

    @Override
    public void addTraining(AddTrainingRequest addTrainingRequest) {
        String traineeUsername = addTrainingRequest.traineeUsername();
        String trainerUsername = addTrainingRequest.trainerUsername();

        String traineeUserId = userServiceClient.getUserIdByUsername(traineeUsername).getBody().userId();
        String trainerUserId = userServiceClient.getUserIdByUsername(trainerUsername).getBody().userId();

        String traineeId = traineeServiceClient.getTraineeIdByUserId(traineeUserId).getBody().traineeId();
        String trainerId = trainerServiceClient.getTrainerIdByUserId(trainerUserId).getBody().trainerId();

        String trainingTypeId = trainerServiceClient.getTrainingTypeIdByTrainerId(trainerId).getBody().trainingTypeId();

        CreateTrainingServiceRequest createTrainingServiceRequest = new CreateTrainingServiceRequest(
                traineeId,
                trainerId,
                addTrainingRequest.trainingName(),
                trainingTypeId,
                addTrainingRequest.date(),
                addTrainingRequest.duration()
        );
        trainingServiceClient.createTraining(createTrainingServiceRequest);
    }

    public GetTraineeTrainingsListResponse getTraineeTrainingsListFallback(GetTraineeTrainingsListRequest request, Throwable exception) {
        return new GetTraineeTrainingsListResponse(Collections.emptyList());
    }

    public GetTrainerTrainingsListResponse getTrainerTrainingsListFallback(GetTrainerTrainingsListRequest getTrainerTrainingsListRequest, Throwable exception) {
        return new GetTrainerTrainingsListResponse(Collections.emptyList());
    }
}
