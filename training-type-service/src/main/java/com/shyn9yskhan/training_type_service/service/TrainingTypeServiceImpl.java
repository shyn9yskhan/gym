package com.shyn9yskhan.training_type_service.service;

import com.shyn9yskhan.training_type_service.dto.*;
import com.shyn9yskhan.training_type_service.entity.TrainingTypeEntity;
import com.shyn9yskhan.training_type_service.repository.TrainingTypeRepository;
import com.shyn9yskhan.training_type_service.service.exception.EntityNotFoundException;
import com.shyn9yskhan.training_type_service.service.exception.TrainingTypeCreationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TrainingTypeServiceImpl implements TrainingTypeService {
    private final TrainingTypeRepository trainingTypeRepository;

    public TrainingTypeServiceImpl(TrainingTypeRepository trainingTypeRepository) {
        this.trainingTypeRepository = trainingTypeRepository;
    }

    @Override
    @Transactional
    public CreateTrainingTypeResponse createTrainingType(CreateTrainingTypeRequest createTrainingTypeRequest) {
        validateCreateTrainingTypeRequest(createTrainingTypeRequest);

        TrainingTypeEntity trainingTypeEntity = new TrainingTypeEntity(
                createTrainingTypeRequest.trainingTypeName()
        );

        try {
            TrainingTypeEntity saved = trainingTypeRepository.save(trainingTypeEntity);

            return new CreateTrainingTypeResponse(
                    saved.getId(),
                    saved.getTrainingTypeName()
            );
        } catch (DataIntegrityViolationException e) {
            throw new TrainingTypeCreationException("Failed to create training type due to data constraint violation", e);
        } catch (Exception e) {
            throw new TrainingTypeCreationException("Failed to create training type", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public GetTrainingTypeResponse getTrainingType(String trainingTypeId) {
        TrainingTypeEntity trainingTypeEntity = trainingTypeRepository.findById(trainingTypeId)
                .orElseThrow(() -> new EntityNotFoundException("Training type not found with ID: " + trainingTypeId));
        return new GetTrainingTypeResponse(trainingTypeEntity.getTrainingTypeName());
    }

    @Override
    @Transactional
    public void deleteTrainingType(String trainingTypeId) {
        if (!trainingTypeRepository.existsById(trainingTypeId)) {
            throw new EntityNotFoundException("Training type not found with ID: " + trainingTypeId);
        }
        trainingTypeRepository.deleteById(trainingTypeId);
    }

    @Override
    @Transactional(readOnly = true)
    public GetAllTrainingTypesResponse getAllTrainingTypes() {
        List<TrainingTypeEntity> trainingTypeEntities = trainingTypeRepository.findAll();
        List<TrainingTypeDto> trainingTypeDtos = convertTrainingTypeEntitiesToDtos(trainingTypeEntities);
        return new GetAllTrainingTypesResponse(trainingTypeDtos);
    }

    private void validateCreateTrainingTypeRequest(CreateTrainingTypeRequest request) {
        if (request == null) throw new IllegalArgumentException("CreateTrainingTypeRequest cannot be null");
        if (request.trainingTypeName() == null || request.trainingTypeName().trim().isEmpty()) {
            throw new IllegalArgumentException("Training type name is required");
        }
    }

    private List<TrainingTypeDto> convertTrainingTypeEntitiesToDtos(List<TrainingTypeEntity> trainingTypeEntities) {
        return trainingTypeEntities.stream()
                .map(trainingTypeEntity -> new TrainingTypeDto(
                        trainingTypeEntity.getId(),
                        trainingTypeEntity.getTrainingTypeName()
                ))
                .collect(Collectors.toList());
    }
}
