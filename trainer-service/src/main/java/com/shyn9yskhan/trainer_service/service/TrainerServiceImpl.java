package com.shyn9yskhan.trainer_service.service;

import com.shyn9yskhan.trainer_service.dto.*;
import com.shyn9yskhan.trainer_service.entity.TrainerEntity;
import com.shyn9yskhan.trainer_service.repository.TrainerRepository;
import com.shyn9yskhan.trainer_service.service.exception.TrainerCreationException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class TrainerServiceImpl implements TrainerService {
    private final TrainerRepository trainerRepository;

    public TrainerServiceImpl(TrainerRepository trainerRepository) {
        this.trainerRepository = trainerRepository;
    }

    @Override
    @Transactional
    public CreateTrainerResponse createTrainer(CreateTrainerRequest createTrainerRequest) {
        validateCreateTrainerRequest(createTrainerRequest);

        TrainerEntity trainerEntity = new TrainerEntity(
                createTrainerRequest.specialization(),
                createTrainerRequest.userId()
        );

        try {
            TrainerEntity savedTrainerEntity = trainerRepository.save(trainerEntity);

            return new CreateTrainerResponse(
                    savedTrainerEntity.getId(),
                    savedTrainerEntity.getSpecialization()
            );
        } catch (DataIntegrityViolationException e) {
            throw new TrainerCreationException("Failed to create trainer due to data constraint violation", e);
        } catch (Exception e) {
            throw new TrainerCreationException("Failed to create trainer", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public GetTrainerResponse getTrainer(String trainerId) {
        TrainerEntity trainerEntity = trainerRepository.findById(trainerId).orElseThrow(() -> new EntityNotFoundException("Trainer not found with ID: " + trainerId));
        return new GetTrainerResponse(trainerEntity.getSpecialization());
    }

    @Override
    public GetTrainingTypeIdByTrainerIdResponse getTrainingTypeIdByTrainerId(String trainerId) {
        String trainingTypeId = trainerRepository.findTrainingTypeIdByTrainerId(trainerId).orElseThrow(() -> new EntityNotFoundException("Training type not found with trainerID: " + trainerId));
        return new GetTrainingTypeIdByTrainerIdResponse(trainingTypeId);
    }

    @Override
    public GetTrainerIdByUserIdResponse getTrainerIdByUserId(String userId) {
        String trainerId = trainerRepository.findIdByUserId(userId).orElseThrow(() -> new EntityNotFoundException("Trainer not found with userID: " + userId));
        return new GetTrainerIdByUserIdResponse(trainerId);
    }

    @Override
    public List<TrainerDto> getTrainersByIds(List<String> trainerIds) {
        List<TrainerEntity> trainerEntities = trainerRepository.findByIdIn(trainerIds);
        List<TrainerDto> trainerDtos = new ArrayList<>();
        for (TrainerEntity trainerEntity : trainerEntities) {
            TrainerDto trainerDto = new TrainerDto(trainerEntity.getId(), trainerEntity.getSpecialization(), trainerEntity.getUserId());
            trainerDtos.add(trainerDto);
        }
        return trainerDtos;
    }

    @Override
    public List<TrainerDto> getTrainersByUserIds(List<String> userIds) {
        List<TrainerEntity> trainerEntities = trainerRepository.findByUserIdIn(userIds);
        List<TrainerDto> trainerDtos = new ArrayList<>();
        for (TrainerEntity trainerEntity : trainerEntities) {
            TrainerDto trainerDto = new TrainerDto(trainerEntity.getId(), trainerEntity.getSpecialization(), trainerEntity.getUserId());
            trainerDtos.add(trainerDto);
        }
        return trainerDtos;
    }

    @Override
    @Transactional
    public UpdateTrainerResponse updateTrainer(String trainerId, UpdateTrainerRequest updateTrainerRequest) {
        validateUpdateTrainerRequest(updateTrainerRequest);

        int rowsAffected = trainerRepository.updateTrainer(
                trainerId,
                updateTrainerRequest.specialization()
        );

        if (rowsAffected == 0) throw new EntityNotFoundException("Trainer not found with ID: " + trainerId);
        TrainerEntity trainerEntity = trainerRepository.findById(trainerId).orElseThrow(() -> new EntityNotFoundException("Trainer not found with ID: " + trainerId));
        return new UpdateTrainerResponse(trainerEntity.getSpecialization());
    }

    @Override
    @Transactional
    public void deleteTrainer(String trainerId) {
        int rowsDeleted = trainerRepository.deleteTrainerById(trainerId);
        if (rowsDeleted == 0) throw new EntityNotFoundException("Trainer not found with ID: " + trainerId);
    }

    @Override
    @Transactional(readOnly = true)
    public GetTrainerResponse getTrainerByUserId(String userId) {
        TrainerEntity trainerEntity = trainerRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("Trainer not found with userId: " + userId));

        return new GetTrainerResponse(trainerEntity.getSpecialization());
    }

    @Override
    @Transactional
    public UpdateTrainerResponse updateTrainerByUserId(String userId, UpdateTrainerRequest updateTrainerRequest) {
        validateUpdateTrainerRequest(updateTrainerRequest);

        int rowsAffected = trainerRepository.updateTrainerByUserId(
                userId,
                updateTrainerRequest.specialization()
        );

        if (rowsAffected == 0) throw new EntityNotFoundException("Trainer not found with userId: " + userId);
        TrainerEntity trainerEntity = trainerRepository.findByUserId(userId).orElseThrow(() -> new EntityNotFoundException("Trainer not found with userId: " + userId));
        return new UpdateTrainerResponse(trainerEntity.getSpecialization());
    }

    @Override
    @Transactional
    public void deleteTrainerByUserId(String userId) {
        int rowsDeleted = trainerRepository.deleteTrainerByUserIdQuery(userId);
        if (rowsDeleted == 0) throw new EntityNotFoundException("Trainer not found with userId: " + userId);
    }

    private void validateCreateTrainerRequest(CreateTrainerRequest request) {
        if (request == null) throw new IllegalArgumentException("CreateTrainerRequest cannot be null");
        if (request.userId() == null || request.userId().trim().isEmpty()) throw new IllegalArgumentException("User ID is required");
        if (request.specialization() == null || request.specialization().trim().isEmpty()) throw new IllegalArgumentException("Specialization is required");
    }

    private void validateUpdateTrainerRequest(UpdateTrainerRequest request) {
        if (request == null) throw new IllegalArgumentException("UpdateTrainerRequest cannot be null");
        if (request.specialization() == null || request.specialization().trim().isEmpty()) throw new IllegalArgumentException("Specialization is required");
    }
}
