package com.shyn9yskhan.user_orchestration_service.service;

import com.shyn9yskhan.user_orchestration_service.client.TraineeServiceClient;
import com.shyn9yskhan.user_orchestration_service.client.TraineeTrainerRelationshipServiceClient;
import com.shyn9yskhan.user_orchestration_service.client.TrainerServiceClient;
import com.shyn9yskhan.user_orchestration_service.client.UserServiceClient;
import com.shyn9yskhan.user_orchestration_service.client.dto.trainee.*;
import com.shyn9yskhan.user_orchestration_service.client.dto.trainer.*;
import com.shyn9yskhan.user_orchestration_service.client.dto.trainer.TrainerServiceDto;
import com.shyn9yskhan.user_orchestration_service.client.dto.user.*;
import com.shyn9yskhan.user_orchestration_service.dto.trainee.*;
import com.shyn9yskhan.user_orchestration_service.dto.trainee_trainer_relationship.GetNotAssignedOnTraineeActiveTrainersResponse;
import com.shyn9yskhan.user_orchestration_service.dto.trainee_trainer_relationship.UpdateTraineesTrainerListRequest;
import com.shyn9yskhan.user_orchestration_service.dto.trainee_trainer_relationship.UpdateTraineesTrainerListResponse;
import com.shyn9yskhan.user_orchestration_service.dto.trainer.*;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class UserOrchestrationServiceImpl implements UserOrchestrationService {
    private final UserServiceClient userServiceClient;
    private final TraineeServiceClient traineeServiceClient;
    private final TrainerServiceClient trainerServiceClient;
    private final TraineeTrainerRelationshipServiceClient traineeTrainerRelationshipServiceClient;

    public UserOrchestrationServiceImpl(UserServiceClient userServiceClient, TraineeServiceClient traineeServiceClient, TrainerServiceClient trainerServiceClient, TraineeTrainerRelationshipServiceClient traineeTrainerRelationshipServiceClient) {
        this.userServiceClient = userServiceClient;
        this.traineeServiceClient = traineeServiceClient;
        this.trainerServiceClient = trainerServiceClient;
        this.traineeTrainerRelationshipServiceClient = traineeTrainerRelationshipServiceClient;
    }

    @Override
    public CreateTraineeResponse createTrainee(CreateTraineeRequest createTraineeRequest) {
        CreateTraineeServiceRequest createTraineeServiceRequest = new CreateTraineeServiceRequest();
        CreateUserServiceRequest createUserServiceRequest = new CreateUserServiceRequest();

        RequestSplitterMerger.splitCreateTraineeRequest(createTraineeRequest, createUserServiceRequest, createTraineeServiceRequest);

        CreateUserServiceResponse createUserServiceResponse = userServiceClient.createUser(createUserServiceRequest).getBody();
        String userId = createUserServiceResponse.getUserId();
        createTraineeServiceRequest.setUserId(userId);
        CreateTraineeServiceResponse createTraineeServiceResponse = traineeServiceClient.createTrainee(createTraineeServiceRequest).getBody();

        return RequestSplitterMerger.mergeResponsesToCreateTraineeResponse(createUserServiceResponse);
    }

    @Override
    public GetTraineeProfileResponse getTraineeProfile(String username) {
        GetUserServiceResponse getUserServiceResponse = userServiceClient.getUserByUsername(username).getBody();
        String userId = getUserServiceResponse.getId();

        GetTraineeByUserIdServiceResponse getTraineeByUserIdServiceResponse = traineeServiceClient.getTraineeByUserId(userId).getBody();
        String traineeId = getTraineeByUserIdServiceResponse.getId();

        List<String> trainerIds = traineeTrainerRelationshipServiceClient.getTrainersForTrainee(traineeId).getBody();
        List<TrainerServiceDto> trainerByIdRespons = trainerServiceClient.getTrainersByIds(trainerIds).getBody();

        List<String> userIds = new ArrayList<>();
        for (TrainerServiceDto trainerServiceDto : trainerByIdRespons) {
            userIds.add(trainerServiceDto.getUserId());
        }

        List<UserDto> userByIdServiceResponses = userServiceClient.getUsersByIds(userIds).getBody();

        return RequestSplitterMerger.mergeResponsesToGetTraineeProfileResponse(getUserServiceResponse, getTraineeByUserIdServiceResponse, trainerByIdRespons, userByIdServiceResponses);
    }

    @Override
    public UpdateTraineeProfileResponse updateTraineeProfile(UpdateTraineeProfileRequest updateTraineeProfileRequest) {
        UpdateUserServiceRequest updateUserServiceRequest = new UpdateUserServiceRequest();
        UpdateTraineeServiceRequest updateTraineeServiceRequest = new UpdateTraineeServiceRequest();

        RequestSplitterMerger.splitUpdateTraineeProfileRequest(updateTraineeProfileRequest, updateUserServiceRequest, updateTraineeServiceRequest);

        UpdateUserByUsernameServiceResponse updateUserByUsernameServiceResponse = userServiceClient.updateUserByUsername(updateTraineeProfileRequest.username(), updateUserServiceRequest).getBody();
        String userId = updateUserByUsernameServiceResponse.getId();
        String username = updateUserByUsernameServiceResponse.getUsername();

        UpdateTraineeByUserIdServiceResponse updateTraineeByUserIdServiceResponse = traineeServiceClient.updateTraineeByUserId(userId, updateTraineeServiceRequest).getBody();

        GetTraineeProfileResponse getTraineeProfileResponse = getTraineeProfile(username);

        return new UpdateTraineeProfileResponse(
                username,
                getTraineeProfileResponse.firstname(),
                getTraineeProfileResponse.lastname(),
                getTraineeProfileResponse.dateOfBirth(),
                getTraineeProfileResponse.address(),
                getTraineeProfileResponse.isActive(),
                getTraineeProfileResponse.trainers()
        );
    }

    @Override
    public void deleteTrainee(String username) {
        DeleteUserByUsernameServiceResponse deleteUserByUsernameServiceResponse = userServiceClient.deleteUserByUsername(username).getBody();
        String userId = deleteUserByUsernameServiceResponse.getId();
        traineeServiceClient.deleteTraineeByUserId(userId);
    }

    @Override
    public CreateTrainerResponse createTrainer(CreateTrainerRequest createTrainerRequest) {
        CreateUserServiceRequest createUserServiceRequest = new CreateUserServiceRequest();
        CreateTrainerServiceRequest createTrainerServiceRequest = new CreateTrainerServiceRequest();

        RequestSplitterMerger.splitCreateTrainerRequest(createTrainerRequest, createUserServiceRequest, createTrainerServiceRequest);

        CreateUserServiceResponse createUserServiceResponse = userServiceClient.createUser(createUserServiceRequest).getBody();
        String userId = createUserServiceResponse.getUserId();

        CreateTrainerServiceResponse createTrainerServiceResponse = trainerServiceClient.createTrainer(createTrainerServiceRequest).getBody();

        return RequestSplitterMerger.mergeResponsesToCreateTrainerResponse(createUserServiceResponse);
    }

    @Override
    public GetTrainerProfileResponse getTrainerProfile(String username) {
        GetUserServiceResponse getUserServiceResponse = userServiceClient.getUserByUsername(username).getBody();
        String userId = getUserServiceResponse.getId();

        TrainerServiceDto getTrainerByUserIdServiceResponse = trainerServiceClient.getTrainerByUserId(userId).getBody();
        String trainerId = getTrainerByUserIdServiceResponse.getId();

        List<String> traineeIds = traineeTrainerRelationshipServiceClient.getTraineesForTrainer(trainerId).getBody();
        List<GetTraineeByIdResponse> getTraineeByIdResponses = traineeServiceClient.getTraineesByIds(traineeIds).getBody();

        List<String> userIds = new ArrayList<>();
        for (GetTraineeByIdResponse getTraineeByIdResponse : getTraineeByIdResponses) {
            userIds.add(getTraineeByIdResponse.getUserId());
        }

        List<UserDto> userByIdServiceResponses = userServiceClient.getUsersByIds(userIds).getBody();

        return RequestSplitterMerger.mergeResponsesToGetTrainerProfileResponse(getUserServiceResponse, getTrainerByUserIdServiceResponse, getTraineeByIdResponses, userByIdServiceResponses);
    }

    @Override
    public UpdateTrainerProfileResponse updateTrainerProfile(UpdateTrainerProfileRequest updateTrainerProfileRequest) {
        UpdateUserServiceRequest updateUserServiceRequest = new UpdateUserServiceRequest();
        UpdateTrainerServiceRequest updateTrainerServiceRequest = new UpdateTrainerServiceRequest();

        RequestSplitterMerger.splitUpdateTrainerProfileRequest(updateTrainerProfileRequest, updateUserServiceRequest, updateTrainerServiceRequest);

        UpdateUserByUsernameServiceResponse updateUserByUsernameServiceResponse = userServiceClient.updateUserByUsername(updateTrainerProfileRequest.username(), updateUserServiceRequest).getBody();
        String userId = updateUserByUsernameServiceResponse.getId();
        String username = updateUserByUsernameServiceResponse.getUsername();

        UpdateTrainerByUserIdServiceResponse updateTrainerByUserIdServiceResponse = trainerServiceClient.updateTrainerByUserId(userId, updateTrainerServiceRequest).getBody();

        GetTrainerProfileResponse getTrainerProfileResponse = getTrainerProfile(username);

        return new UpdateTrainerProfileResponse(
                username,
                getTrainerProfileResponse.firstname(),
                getTrainerProfileResponse.lastname(),
                getTrainerProfileResponse.trainingTypeId(),
                getTrainerProfileResponse.isActive(),
                getTrainerProfileResponse.trainees()
        );
    }

    @Override
    public void deleteTrainer(String username) {
        DeleteUserByUsernameServiceResponse deleteUserByUsernameServiceResponse = userServiceClient.deleteUserByUsername(username).getBody();
        String userId = deleteUserByUsernameServiceResponse.getId();
        trainerServiceClient.deleteTrainerByUserId(userId);
    }

    @Override
    public GetNotAssignedOnTraineeActiveTrainersResponse getNotAssignedOnTraineeActiveTrainers(String username) {
        List<UserDto> allActiveUsers = userServiceClient.getAllActiveUsers().getBody();

        Map<String, UserDto> userById = allActiveUsers.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(UserDto::getUserId, Function.identity(), (a, b) -> a));

        List<String> userIds = new ArrayList<>(userById.keySet());
        List<TrainerServiceDto> allActiveTrainers = Optional.ofNullable(trainerServiceClient.getTrainersByIds(userIds).getBody())
                .orElse(Collections.emptyList());

        GetTraineeProfileResponse traineeProfile = getTraineeProfile(username);
        List<com.shyn9yskhan.user_orchestration_service.dto.trainer.TrainerDto> assignedOnTraineeTrainers = Optional.ofNullable(traineeProfile)
                .map(GetTraineeProfileResponse::trainers)
                .orElse(Collections.emptyList());

        Set<String> assignedUsernames = assignedOnTraineeTrainers.stream()
                .filter(Objects::nonNull)
                .map(com.shyn9yskhan.user_orchestration_service.dto.trainer.TrainerDto::username)
                .collect(Collectors.toSet());

        List<com.shyn9yskhan.user_orchestration_service.dto.trainer.TrainerDto> notAssigned = allActiveTrainers.stream()
                .filter(Objects::nonNull)
                .map(trainer -> {
                    UserDto user = userById.get(trainer.getUserId());
                    if (user == null) return null;
                    if (assignedUsernames.contains(user.getUsername())) return null;
                    return new com.shyn9yskhan.user_orchestration_service.dto.trainer.TrainerDto(
                            user.getUsername(),
                            user.getFirstname(),
                            user.getLastname(),
                            trainer.getSpecialization()
                    );
                })
                .filter(Objects::nonNull)
                .toList();

        return new GetNotAssignedOnTraineeActiveTrainersResponse(notAssigned);
    }

    @Override
    public UpdateTraineesTrainerListResponse updateTraineesTrainerList(String username, UpdateTraineesTrainerListRequest updateTraineesTrainerListRequest) {
        GetUserServiceResponse getUserServiceResponse = userServiceClient.getUserByUsername(username).getBody();
        String traineeUserId = getUserServiceResponse.getId();
        GetTraineeByUserIdServiceResponse getTraineeByUserIdServiceResponse = traineeServiceClient.getTraineeByUserId(traineeUserId).getBody();
        String traineeId = getTraineeByUserIdServiceResponse.getId();

        List<String> trainersUsernames = updateTraineesTrainerListRequest.trainersUsernames();
        List<UserDto> userDtos = userServiceClient.getUsersByUsernames(trainersUsernames).getBody();

        List<String> trainersUserIds = new ArrayList<>();

        for (UserDto userDto : userDtos) {
            trainersUserIds.add(userDto.getUserId());
        }

        List<TrainerServiceDto> trainerServiceDtos = trainerServiceClient.getTrainersByUserIds(trainersUserIds).getBody();
        List<String> trainerIds = new ArrayList<>();
        for (TrainerServiceDto trainerServiceDto : trainerServiceDtos) {
            trainerIds.add(trainerServiceDto.getId());
        }

        traineeTrainerRelationshipServiceClient.associateTraineeWithTrainers(traineeId, trainerIds);
        List<String> trainersIds = traineeTrainerRelationshipServiceClient.getTrainersForTrainee(traineeId).getBody();
        List<TrainerServiceDto> trainers = trainerServiceClient.getTrainersByIds(trainersIds).getBody();

        List<String> userIds = new ArrayList<>();
        for (TrainerServiceDto trainerServiceDto : trainers) {
            userIds.add(trainerServiceDto.getUserId());
        }
        List<UserDto> users = userServiceClient.getUsersByIds(userIds).getBody();

        Map<String, UserDto> userById = users.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(UserDto::getUserId, Function.identity(), (a, b) -> a));

        List<TrainerDto> trainerDtos = trainers.stream()
                .map(trainerServiceDto -> {
                    UserDto userDto = userById.get(trainerServiceDto.getUserId());
                    if (userDto == null) {
                        return null;
                    }
                    return new TrainerDto(
                            userDto.getUsername(),
                            userDto.getFirstname(),
                            userDto.getLastname(),
                            trainerServiceDto.getSpecialization()
                    );
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        return new UpdateTraineesTrainerListResponse(trainerDtos);
    }
}
