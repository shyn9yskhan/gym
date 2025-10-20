package com.shyn9yskhan.trainee_service.service;

import com.shyn9yskhan.trainee_service.dto.*;
import com.shyn9yskhan.trainee_service.entity.TraineeEntity;
import com.shyn9yskhan.trainee_service.service.exception.EntityNotFoundException;
import com.shyn9yskhan.trainee_service.service.exception.TraineeCreationException;
import com.shyn9yskhan.trainee_service.repository.TraineeRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class TraineeServiceImpl implements TraineeService {
    private final TraineeRepository traineeRepository;

    public TraineeServiceImpl(TraineeRepository traineeRepository) {
        this.traineeRepository = traineeRepository;
    }

    @Override
    @Transactional
    public CreateTraineeResponse createTrainee(CreateTraineeRequest createTraineeRequest) {
        validateCreateTraineeRequest(createTraineeRequest);

        TraineeEntity traineeEntity = new TraineeEntity(
                createTraineeRequest.dateOfBirth(),
                createTraineeRequest.address(),
                createTraineeRequest.userId()
        );

        try {
            TraineeEntity savedTraineeEntity = traineeRepository.save(traineeEntity);

            return new CreateTraineeResponse(
                    savedTraineeEntity.getId(),
                    savedTraineeEntity.getDateOfBirth(),
                    savedTraineeEntity.getAddress()
            );
        } catch (DataIntegrityViolationException e) {
            throw new TraineeCreationException("Failed to create trainee due to data constraint violation", e);
        } catch (Exception e) {
            throw new TraineeCreationException("Failed to create trainee", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public GetTraineeResponse getTrainee(String traineeId) {
        TraineeEntity traineeEntity = traineeRepository.findById(traineeId).orElseThrow(() -> new EntityNotFoundException("Trainee not found with ID: " + traineeId));
        return new GetTraineeResponse(traineeEntity.getDateOfBirth(), traineeEntity.getAddress());
    }

    @Override
    @Transactional(readOnly = true)
    public GetTraineeIdByUserIdResponse getTraineeIdByUserId(String userId) {
        String traineeId = traineeRepository.findIdByUserId(userId).orElseThrow(() -> new EntityNotFoundException("TraineeID not found with UserID: " + userId));
        return new GetTraineeIdByUserIdResponse(traineeId);
    }

    @Override
    public List<TraineeDto> getTraineesByIds(List<String> traineeIds) {
        List<TraineeEntity> traineeEntities = traineeRepository.findByIdIn(traineeIds);
        List<TraineeDto> traineeDtos = new ArrayList<>();
        for (TraineeEntity traineeEntity : traineeEntities) {
            TraineeDto traineeDto = new TraineeDto(traineeEntity.getId(), traineeEntity.getDateOfBirth(), traineeEntity.getAddress(), traineeEntity.getUserId());
            traineeDtos.add(traineeDto);
        }
        return traineeDtos;
    }

    @Override
    @Transactional
    public UpdateTraineeResponse updateTrainee(String traineeId, UpdateTraineeRequest updateTraineeRequest) {
        validateUpdateTraineeRequest(updateTraineeRequest);

        int rowsAffected = traineeRepository.updateTrainee(
                traineeId,
                updateTraineeRequest.dateOfBirth(),
                updateTraineeRequest.address()
        );

        if (rowsAffected == 0) throw new EntityNotFoundException("Trainee not found with ID: " + traineeId);
        TraineeEntity traineeEntity = traineeRepository.findById(traineeId).orElseThrow(() -> new EntityNotFoundException("Trainee not found with ID: " + traineeId));
        return new UpdateTraineeResponse(traineeEntity.getDateOfBirth(), traineeEntity.getAddress());
    }

    @Override
    @Transactional
    public void deleteTrainee(String traineeId) {
        int rowsDeleted = traineeRepository.deleteTraineeById(traineeId);
        if (rowsDeleted == 0) throw new EntityNotFoundException("Trainee not found with ID: " + traineeId);
    }

    @Override
    @Transactional(readOnly = true)
    public GetTraineeByUserIdResponse getTraineeByUserId(String userId) {
        TraineeEntity traineeEntity = traineeRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("Trainee not found with traineeId: " + userId));

        return new GetTraineeByUserIdResponse(traineeEntity.getId(), traineeEntity.getDateOfBirth(), traineeEntity.getAddress(), traineeEntity.getUserId());
    }

    @Override
    @Transactional
    public UpdateTraineeByUserIdResponse updateTraineeByUserId(String userId, UpdateTraineeRequest updateTraineeRequest) {
        validateUpdateTraineeRequest(updateTraineeRequest);

        int rowsAffected = traineeRepository.updateTraineeByUserId(
                userId,
                updateTraineeRequest.dateOfBirth(),
                updateTraineeRequest.address()
        );

        if (rowsAffected == 0) throw new EntityNotFoundException("Trainee not found with traineeId: " + userId);
        TraineeEntity traineeEntity = traineeRepository.findByUserId(userId).orElseThrow(() -> new EntityNotFoundException("Trainee not found with traineeId: " + userId));
        return new UpdateTraineeByUserIdResponse(traineeEntity.getId(), traineeEntity.getDateOfBirth(), traineeEntity.getAddress());
    }

    @Override
    @Transactional
    public void deleteTraineeByUserId(String userId) {
        int rowsDeleted = traineeRepository.deleteTraineeByUserIdQuery(userId);
        if (rowsDeleted == 0) throw new EntityNotFoundException("Trainee not found with traineeId: " + userId);
    }

    private void validateCreateTraineeRequest(CreateTraineeRequest request) {
        if (request == null) throw new IllegalArgumentException("CreateTraineeRequest cannot be null");
        if (request.userId() == null || request.userId().trim().isEmpty()) throw new IllegalArgumentException("User ID is required");
        if (request.address() == null || request.address().trim().isEmpty()) throw new IllegalArgumentException("Address is required");
    }

    private void validateUpdateTraineeRequest(UpdateTraineeRequest request) {
        if (request == null) throw new IllegalArgumentException("UpdateTraineeRequest cannot be null");
        if (request.address() == null || request.address().trim().isEmpty()) throw new IllegalArgumentException("Address is required");
    }
}
