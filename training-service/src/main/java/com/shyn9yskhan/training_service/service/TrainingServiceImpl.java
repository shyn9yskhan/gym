package com.shyn9yskhan.training_service.service;

import com.shyn9yskhan.training_service.dto.*;
import com.shyn9yskhan.training_service.entity.TrainingEntity;
import com.shyn9yskhan.training_service.repository.TrainingRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TrainingServiceImpl implements TrainingService {
    private final TrainingRepository trainingRepository;

    public TrainingServiceImpl(TrainingRepository trainingRepository) {
        this.trainingRepository = trainingRepository;
    }

    @Override
    @Transactional
    public CreateTrainingResponse createTraining(CreateTrainingRequest createTrainingRequest) {
        validateCreateTrainingRequest(createTrainingRequest);

        TrainingEntity trainingEntity = new TrainingEntity(
                createTrainingRequest.traineeId(),
                createTrainingRequest.trainerId(),
                createTrainingRequest.trainingName(),
                createTrainingRequest.trainingTypeId(),
                createTrainingRequest.date(),
                createTrainingRequest.duration()
        );

        TrainingEntity saved = trainingRepository.save(trainingEntity);
        return new CreateTrainingResponse(
                saved.getId(),
                saved.getTraineeId(),
                saved.getTrainerId(),
                saved.getTrainingName(),
                saved.getTrainingTypeId(),
                saved.getDate(),
                saved.getDuration()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public GetTrainingResponse getTraining(String trainingId) {
        TrainingEntity trainingEntity = trainingRepository.findById(trainingId).orElseThrow(() -> new EntityNotFoundException("Training not found with ID: " + trainingId));
        return new GetTrainingResponse(
                trainingEntity.getTraineeId(),
                trainingEntity.getTrainerId(),
                trainingEntity.getTrainingName(),
                trainingEntity.getTrainingTypeId(),
                trainingEntity.getDate(),
                trainingEntity.getDuration()
        );
    }

    @Override
    @Transactional
    public UpdateTrainingResponse updateTraining(String trainingId, UpdateTrainingRequest updateTrainingRequest) {
        validateUpdateTrainingRequest(updateTrainingRequest);

        TrainingEntity trainingEntity = trainingRepository.findById(trainingId).orElseThrow(() -> new EntityNotFoundException("Training not found with ID: " + trainingId));

        trainingEntity.setTraineeId(updateTrainingRequest.traineeId());
        trainingEntity.setTrainerId(updateTrainingRequest.trainerId());
        trainingEntity.setTrainingName(updateTrainingRequest.trainingName());
        trainingEntity.setTrainingTypeId(updateTrainingRequest.trainingTypeId());
        trainingEntity.setDate(updateTrainingRequest.date());
        trainingEntity.setDuration(updateTrainingRequest.duration());

        TrainingEntity updatedTrainingEntity = trainingRepository.save(trainingEntity);

        return new UpdateTrainingResponse(
                updatedTrainingEntity.getTraineeId(),
                updatedTrainingEntity.getTrainerId(),
                updatedTrainingEntity.getTrainingName(),
                updatedTrainingEntity.getTrainingTypeId(),
                updatedTrainingEntity.getDate(),
                updatedTrainingEntity.getDuration()
        );
    }

    @Override
    @Transactional
    public void deleteTraining(String trainingId) {
        if (!trainingRepository.existsById(trainingId)) {
            throw new EntityNotFoundException("Training not found with ID: " + trainingId);
        }
        trainingRepository.deleteById(trainingId);
    }

    @Override
    @Transactional(readOnly = true)
    public GetTraineeTrainingsResponse getTrainingsByTraineeId(String traineeId) {
        if (traineeId == null || traineeId.trim().isEmpty()) {
            throw new IllegalArgumentException("Trainee ID is required");
        }

        List<TrainingEntity> trainingEntities = trainingRepository.findByTraineeId(traineeId);
        List<TrainingDto> trainings = convertTrainingEntitiesToDtos(trainingEntities);
        return new GetTraineeTrainingsResponse(trainings);
    }

    @Override
    @Transactional(readOnly = true)
    public GetTrainerTrainingsResponse getTrainingsByTrainerId(String trainerId) {
        if (trainerId == null || trainerId.trim().isEmpty()) {
            throw new IllegalArgumentException("Trainer ID is required");
        }

        List<TrainingEntity> trainingEntities = trainingRepository.findByTrainerId(trainerId);
        List<TrainingDto> trainings = convertTrainingEntitiesToDtos(trainingEntities);
        return new GetTrainerTrainingsResponse(trainings);
    }

    private void validateCreateTrainingRequest(CreateTrainingRequest request) {
        if (request == null) throw new IllegalArgumentException("CreateTrainingRequest cannot be null");
        if (request.traineeId() == null || request.traineeId().trim().isEmpty()) throw new IllegalArgumentException("Trainee ID is required");
        if (request.trainerId() == null || request.trainerId().trim().isEmpty()) throw new IllegalArgumentException("Trainer ID is required");
        if (request.trainingName() == null || request.trainingName().trim().isEmpty()) throw new IllegalArgumentException("Training name is required");
        if (request.trainingTypeId() == null || request.trainingTypeId().trim().isEmpty()) throw new IllegalArgumentException("Training type ID is required");
        if (request.date() == null) throw new IllegalArgumentException("Date is required");
        if (request.duration() <= 0) throw new IllegalArgumentException("Duration must be positive");
    }

    private void validateUpdateTrainingRequest(UpdateTrainingRequest request) {
        if (request == null) throw new IllegalArgumentException("UpdateTrainingRequest cannot be null");
        if (request.traineeId() == null || request.traineeId().trim().isEmpty()) throw new IllegalArgumentException("Trainee ID is required");
        if (request.trainerId() == null || request.trainerId().trim().isEmpty()) throw new IllegalArgumentException("Trainer ID is required");
        if (request.trainingName() == null || request.trainingName().trim().isEmpty()) throw new IllegalArgumentException("Training name is required");
        if (request.trainingTypeId() == null || request.trainingTypeId().trim().isEmpty()) throw new IllegalArgumentException("Training type ID is required");
        if (request.date() == null) throw new IllegalArgumentException("Date is required");
        if (request.duration() <= 0) throw new IllegalArgumentException("Duration must be positive");
    }

    private List<TrainingDto> convertTrainingEntitiesToDtos(List<TrainingEntity> trainingEntities) {
        return trainingEntities.stream()
                .map(trainingEntity -> new TrainingDto(
                        trainingEntity.getTraineeId(),
                        trainingEntity.getTrainerId(),
                        trainingEntity.getTrainingName(),
                        trainingEntity.getTrainingTypeId(),
                        trainingEntity.getDate(),
                        trainingEntity.getDuration()
                ))
                .collect(Collectors.toList());
    }
}
