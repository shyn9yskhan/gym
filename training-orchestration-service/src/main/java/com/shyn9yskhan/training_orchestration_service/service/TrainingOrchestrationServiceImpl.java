package com.shyn9yskhan.training_orchestration_service.service;

import com.shyn9yskhan.training_orchestration_service.client.dto.*;
import com.shyn9yskhan.training_orchestration_service.dto.*;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessagePostProcessor;
import org.springframework.stereotype.Service;

import jakarta.jms.JMSException;
import jakarta.jms.Message;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@Service
public class TrainingOrchestrationServiceImpl implements TrainingOrchestrationService {

    private final JmsTemplate jmsTemplate;

    private static final String USER_QUEUE = "user.request.queue";
    private static final String TRAINEE_QUEUE = "trainee.request.queue";
    private static final String TRAINER_QUEUE = "trainer.request.queue";
    private static final String TRAINING_QUEUE = "training.request.queue";

    private static final String OPERATION_HEADER = "operation";

    private final ExecutorService executor = Executors.newCachedThreadPool();

    public TrainingOrchestrationServiceImpl(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    @Override
    @CircuitBreaker(name = "trainingServiceClient", fallbackMethod = "getTraineeTrainingsListFallback")
    public GetTraineeTrainingsListResponse getTraineeTrainingsList(GetTraineeTrainingsListRequest request) {
        String username = request.username();

        GetUserIdByUsernameServiceResponse userIdResponse =
                sendRequestAndReceive(USER_QUEUE,
                        new GetUserIdByUsernameServiceRequest(username),
                        GetUserIdByUsernameServiceResponse.class,
                        "GetUserIdByUsername");
        if (userIdResponse == null) return new GetTraineeTrainingsListResponse(Collections.emptyList());
        String userId = userIdResponse.userId();

        GetTraineeIdByUserIdServiceResponse traineeIdResponse =
                sendRequestAndReceive(TRAINEE_QUEUE,
                        new GetTraineeIdByUserIdsRequest(userId),
                        GetTraineeIdByUserIdServiceResponse.class,
                        "GetTraineeIdByUserId");
        if (traineeIdResponse == null) return new GetTraineeTrainingsListResponse(Collections.emptyList());
        String traineeId = traineeIdResponse.traineeId();

        GetTraineeTrainingsServiceResponse trainingsResponse =
                sendRequestAndReceive(TRAINING_QUEUE,
                        new GetTraineeTrainingsRequest(traineeId),
                        GetTraineeTrainingsServiceResponse.class,
                        "GetTrainingsByTraineeId");

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
                    if (requestedTrainingTypeId != null && !requestedTrainingTypeId.equals(training.trainingTypeId()))
                        return false;
                    return true;
                })
                .collect(Collectors.toList());

        return new GetTraineeTrainingsListResponse(filteredTrainings);
    }

    @Override
    @CircuitBreaker(name = "trainingServiceClient", fallbackMethod = "getTrainerTrainingsListFallback")
    public GetTrainerTrainingsListResponse getTrainerTrainingsList(GetTrainerTrainingsListRequest request) {
        String username = request.username();

        GetUserIdByUsernameServiceResponse userIdResponse =
                sendRequestAndReceive(USER_QUEUE,
                        new GetUserIdByUsernameServiceRequest(username),
                        GetUserIdByUsernameServiceResponse.class,
                        "GetUserIdByUsername");
        if (userIdResponse == null) return new GetTrainerTrainingsListResponse(Collections.emptyList());
        String userId = userIdResponse.userId();

        GetTrainerIdByUserIdServiceResponse trainerIdResponse =
                sendRequestAndReceive(TRAINER_QUEUE,
                        new GetTrainerIdByUserIdServiceRequest(userId),
                        GetTrainerIdByUserIdServiceResponse.class,
                        "GetTrainerIdByUserId");
        if (trainerIdResponse == null) return new GetTrainerTrainingsListResponse(Collections.emptyList());
        String trainerId = trainerIdResponse.trainerId();

        GetTrainerTrainingsServiceResponse trainingsResponse =
                sendRequestAndReceive(TRAINING_QUEUE,
                        new GetTrainerTrainingsServiceRequest(trainerId),
                        GetTrainerTrainingsServiceResponse.class,
                        "GetTrainingsByTrainerId");

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
                    if (requestedTrainingTypeId != null && !requestedTrainingTypeId.equals(training.trainingTypeId()))
                        return false;
                    return true;
                })
                .collect(Collectors.toList());

        return new GetTrainerTrainingsListResponse(filteredTrainings);
    }

    @Override
    public void addTraining(AddTrainingRequest addTrainingRequest) {
        String traineeUsername = addTrainingRequest.traineeUsername();
        String trainerUsername = addTrainingRequest.trainerUsername();

        CompletableFuture<GetUserIdByUsernameServiceResponse> traineeUserIdFuture =
                CompletableFuture.supplyAsync(() ->
                        sendRequestAndReceive(USER_QUEUE,
                                new GetUserIdByUsernameServiceRequest(traineeUsername),
                                GetUserIdByUsernameServiceResponse.class,
                                "GetUserIdByUsername"), executor);

        CompletableFuture<GetUserIdByUsernameServiceResponse> trainerUserIdFuture =
                CompletableFuture.supplyAsync(() ->
                        sendRequestAndReceive(USER_QUEUE,
                                new GetUserIdByUsernameServiceRequest(trainerUsername),
                                GetUserIdByUsernameServiceResponse.class,
                                "GetUserIdByUsername"), executor);

        try {
            GetUserIdByUsernameServiceResponse traineeUserResp = traineeUserIdFuture.get();
            GetUserIdByUsernameServiceResponse trainerUserResp = trainerUserIdFuture.get();

            if (traineeUserResp == null || trainerUserResp == null) {
                return;
            }

            String traineeUserId = traineeUserResp.userId();
            String trainerUserId = trainerUserResp.userId();

            GetTraineeIdByUserIdServiceResponse traineeIdResp =
                    sendRequestAndReceive(TRAINEE_QUEUE,
                            new GetTraineeIdByUserIdServiceRequest(traineeUserId),
                            GetTraineeIdByUserIdServiceResponse.class,
                            "GetTraineeIdByUserId");
            if (traineeIdResp == null) return;
            String traineeId = traineeIdResp.traineeId();

            GetTrainerIdByUserIdServiceResponse trainerIdResp =
                    sendRequestAndReceive(TRAINER_QUEUE,
                            new GetTrainerIdByUserIdServiceRequest(trainerUserId),
                            GetTrainerIdByUserIdServiceResponse.class,
                            "GetTrainerIdByUserId");
            if (trainerIdResp == null) return;
            String trainerId = trainerIdResp.trainerId();

            GetTrainingTypeIdByTrainerIdServiceResponse trainingTypeResp =
                    sendRequestAndReceive(TRAINER_QUEUE,
                            new GetTrainingTypeIdByTrainerIdServiceRequest(trainerId),
                            GetTrainingTypeIdByTrainerIdServiceResponse.class,
                            "GetTrainingTypeIdByTrainerId");
            if (trainingTypeResp == null) return;
            String trainingTypeId = trainingTypeResp.trainingTypeId();

            CreateTrainingServiceRequest createTrainingServiceRequest = new CreateTrainingServiceRequest(
                    traineeId,
                    trainerId,
                    addTrainingRequest.trainingName(),
                    trainingTypeId,
                    addTrainingRequest.date(),
                    addTrainingRequest.duration()
            );

            jmsTemplate.convertAndSend(TRAINING_QUEUE, createTrainingServiceRequest, message -> {
                try {
                    message.setStringProperty(OPERATION_HEADER, "CreateTraining");
                } catch (JMSException ignored) {
                }
                return message;
            });

        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        } catch (ExecutionException ignored) {
        }
    }

    @SuppressWarnings("unchecked")
    private <T> T sendRequestAndReceive(String destination, Object request, Class<T> responseType, String operation) {
        try {
            Object raw = jmsTemplate.convertSendAndReceive(destination, request, message -> {
                try {
                    message.setStringProperty(OPERATION_HEADER, operation);
                } catch (JMSException ignored) {
                }
                return message;
            });
            if (raw == null) return null;
            if (responseType.isInstance(raw)) {
                return (T) raw;
            } else {
                return null;
            }
        } catch (Exception ex) {
            return null;
        }
    }

    public GetTraineeTrainingsListResponse getTraineeTrainingsListFallback(GetTraineeTrainingsListRequest request, Throwable exception) {
        return new GetTraineeTrainingsListResponse(Collections.emptyList());
    }

    public GetTrainerTrainingsListResponse getTrainerTrainingsListFallback(GetTrainerTrainingsListRequest request, Throwable exception) {
        return new GetTrainerTrainingsListResponse(Collections.emptyList());
    }
}
