package com.shyn9yskhan.user_orchestration_service.service;

import com.shyn9yskhan.user_orchestration_service.client.dto.trainee.CreateTraineeServiceRequest;
import com.shyn9yskhan.user_orchestration_service.client.dto.trainee.GetTraineeByIdResponse;
import com.shyn9yskhan.user_orchestration_service.client.dto.trainee.GetTraineeByUserIdServiceResponse;
import com.shyn9yskhan.user_orchestration_service.client.dto.trainee.UpdateTraineeServiceRequest;
import com.shyn9yskhan.user_orchestration_service.client.dto.trainer.*;
import com.shyn9yskhan.user_orchestration_service.client.dto.trainer.TrainerServiceDto;
import com.shyn9yskhan.user_orchestration_service.client.dto.user.*;
import com.shyn9yskhan.user_orchestration_service.dto.trainee.*;
import com.shyn9yskhan.user_orchestration_service.dto.trainer.*;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class RequestSplitterMerger {

    public static void splitCreateTraineeRequest(CreateTraineeRequest createTraineeRequest,
                                                 CreateUserServiceRequest createUserServiceRequest,
                                                 CreateTraineeServiceRequest createTraineeServiceRequest) {
        createUserServiceRequest.setFirstname(createTraineeRequest.firstname());
        createUserServiceRequest.setLastname(createTraineeRequest.lastname());
        createTraineeServiceRequest.setDateOfBirth(createTraineeRequest.dateOfBirth());
        createTraineeServiceRequest.setAddress(createTraineeRequest.address());
    }

    public static CreateTraineeResponse mergeResponsesToCreateTraineeResponse(CreateUserServiceResponse createUserServiceResponse) {
        return new CreateTraineeResponse(createUserServiceResponse.getUsername(), createUserServiceResponse.getPassword());
    }

    public static GetTraineeProfileResponse mergeResponsesToGetTraineeProfileResponse(GetUserServiceResponse getUserServiceResponse,
                                                                                      GetTraineeByUserIdServiceResponse getTraineeByUserIdServiceResponse,
                                                                                      List<TrainerServiceDto> trainerByIdRespons,
                                                                                      List<UserDto> userByIdServiceRespons) {

        List<TrainerServiceDto> trainers = (trainerByIdRespons == null) ? Collections.emptyList() : trainerByIdRespons;
        List<UserDto> users = (userByIdServiceRespons == null) ? Collections.emptyList() : userByIdServiceRespons;

        Map<String, UserDto> userById = users.stream().collect(Collectors.toMap(UserDto::getUserId, Function.identity(), (a, b) -> a));

        List<com.shyn9yskhan.user_orchestration_service.dto.trainer.TrainerDto> trainerDtos = trainers.stream()
                .map(trainer -> {
                    UserDto user = userById.get(trainer.getUserId());
                    if (user == null) return null;
                    return new com.shyn9yskhan.user_orchestration_service.dto.trainer.TrainerDto(
                            user.getUsername(),
                            user.getFirstname(),
                            user.getLastname(),
                            trainer.getSpecialization()
                    );
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        return new GetTraineeProfileResponse(
                getUserServiceResponse.getFirstname(),
                getUserServiceResponse.getLastname(),
                getTraineeByUserIdServiceResponse.getDateOfBirth(),
                getTraineeByUserIdServiceResponse.getAddress(),
                getUserServiceResponse.isActive(),
                trainerDtos
        );
    }

    public static void splitUpdateTraineeProfileRequest(UpdateTraineeProfileRequest updateTraineeProfileRequest,
                                                 UpdateUserServiceRequest updateUserServiceRequest,
                                                 UpdateTraineeServiceRequest updateTraineeServiceRequest) {
        updateUserServiceRequest.setFirstname(updateTraineeProfileRequest.firstname());
        updateUserServiceRequest.setLastname(updateTraineeProfileRequest.lastname());
        updateUserServiceRequest.setActive(updateTraineeProfileRequest.isActive());

        updateTraineeServiceRequest.setDateOfBirth(updateTraineeProfileRequest.dateOfBirth());
        updateTraineeServiceRequest.setAddress(updateTraineeProfileRequest.address());
    }

    public static void splitCreateTrainerRequest(CreateTrainerRequest createTrainerRequest,
                                                 CreateUserServiceRequest createUserServiceRequest,
                                                 CreateTrainerServiceRequest createTrainerServiceRequest) {
        createUserServiceRequest.setFirstname(createTrainerRequest.firstname());
        createUserServiceRequest.setLastname(createTrainerRequest.lastname());

        createTrainerServiceRequest.setSpecialization(createTrainerRequest.trainingTypeId());
    }

    public static CreateTrainerResponse mergeResponsesToCreateTrainerResponse(CreateUserServiceResponse createUserServiceResponse) {
        return new CreateTrainerResponse(createUserServiceResponse.getUsername(), createUserServiceResponse.getPassword());
    }

    public static GetTrainerProfileResponse mergeResponsesToGetTrainerProfileResponse(
            GetUserServiceResponse getUserServiceResponse,
            TrainerServiceDto getTrainerByUserIdServiceResponse,
            List<GetTraineeByIdResponse> getTraineeByIdResponses,
            List<UserDto> userByIdServiceRespons) {

        List<GetTraineeByIdResponse> trainees = (getTraineeByIdResponses == null) ? Collections.emptyList() : getTraineeByIdResponses;
        List<UserDto> users = (userByIdServiceRespons == null) ? Collections.emptyList() : userByIdServiceRespons;

        Map<String, UserDto> userById = users.stream()
                .collect(Collectors.toMap(UserDto::getUserId, Function.identity(), (a, b) -> a));

        List<TraineeDto> traineeDtos = trainees.stream()
                .map(trainee -> {
                    UserDto user = userById.get(trainee.getUserId());
                    if (user == null) return null;
                    return new TraineeDto(
                            user.getUsername(),
                            user.getFirstname(),
                            user.getLastname(),
                            trainee.getDateOfBirth(),
                            trainee.getAddress()
                    );
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        return new GetTrainerProfileResponse(
                getUserServiceResponse.getFirstname(),
                getUserServiceResponse.getLastname(),
                getTrainerByUserIdServiceResponse.getSpecialization(),
                getUserServiceResponse.isActive(),
                traineeDtos
        );
    }

    public static void splitUpdateTrainerProfileRequest(UpdateTrainerProfileRequest updateTrainerProfileRequest,
                                                   UpdateUserServiceRequest updateUserServiceRequest,
                                                   UpdateTrainerServiceRequest updateTrainerServiceRequest) {
        updateUserServiceRequest.setFirstname(updateTrainerProfileRequest.firstname());
        updateUserServiceRequest.setLastname(updateTrainerProfileRequest.lastname());
        updateUserServiceRequest.setActive(updateTrainerProfileRequest.isActive());

        updateTrainerServiceRequest.setSpecialization(updateTrainerProfileRequest.trainingTypeId());
    }
}
