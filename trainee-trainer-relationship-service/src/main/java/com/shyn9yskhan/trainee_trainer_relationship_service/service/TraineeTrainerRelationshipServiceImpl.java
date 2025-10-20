package com.shyn9yskhan.trainee_trainer_relationship_service.service;

import com.shyn9yskhan.trainee_trainer_relationship_service.entity.TraineeTrainerRelationshipEntity;
import com.shyn9yskhan.trainee_trainer_relationship_service.entity.TraineeTrainerRelationshipKey;
import com.shyn9yskhan.trainee_trainer_relationship_service.repository.TraineeTrainerRelationshipRepository;
import com.shyn9yskhan.trainee_trainer_relationship_service.service.exception.RelationshipCreationException;
import com.shyn9yskhan.trainee_trainer_relationship_service.service.exception.RelationshipNotFoundException;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class TraineeTrainerRelationshipServiceImpl implements TraineeTrainerRelationshipService {
    private final TraineeTrainerRelationshipRepository traineeTrainerRelationshipRepository;

    public TraineeTrainerRelationshipServiceImpl(TraineeTrainerRelationshipRepository traineeTrainerRelationshipRepository) {
        this.traineeTrainerRelationshipRepository = traineeTrainerRelationshipRepository;
    }

    @Override
    @Transactional
    public void associate(String traineeId, String trainerId) {
        validateId(traineeId, "traineeId");
        validateId(trainerId, "trainerId");

        if (exists(traineeId, trainerId)) return;

        TraineeTrainerRelationshipKey key = new TraineeTrainerRelationshipKey(traineeId, trainerId);
        TraineeTrainerRelationshipEntity entity = new TraineeTrainerRelationshipEntity(key);

        try {
            traineeTrainerRelationshipRepository.save(entity);
        } catch (DataIntegrityViolationException e) {
            throw new RelationshipCreationException("Failed to associate trainee and trainer due to data constraint violation", e);
        } catch (DataAccessException e) {
            throw new RelationshipCreationException("Failed to associate trainee and trainer", e);
        }
    }

    @Override
    public void associateTraineeWithTrainers(String traineeId, List<String> trainerIds) {
        validateId(traineeId, "traineeId");

        if (trainerIds == null || trainerIds.isEmpty()) return;

        List<String> uniqueTrainerIds = trainerIds.stream()
                .filter(Objects::nonNull)
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .distinct()
                .toList();

        if (uniqueTrainerIds.isEmpty()) return;

        List<TraineeTrainerRelationshipEntity> entities = uniqueTrainerIds.stream()
                .map(tid -> new TraineeTrainerRelationshipEntity(new TraineeTrainerRelationshipKey(traineeId, tid)))
                .collect(Collectors.toList());

        try {
            traineeTrainerRelationshipRepository.saveAll(entities);
        } catch (DataIntegrityViolationException e) {
            throw new RelationshipCreationException("associateTraineeWithTrainers: constraint violation while saving associations (traineeId=" + traineeId + ", trainerCount=" + uniqueTrainerIds.size() + "). " +
                            "This is likely due to existing associations or concurrent inserts. Exception: {}", e);
        }
    }

    @Override
    @Transactional
    public void disassociate(String traineeId, String trainerId) {
        validateId(traineeId, "traineeId");
        validateId(trainerId, "trainerId");

        int rowsDeleted = traineeTrainerRelationshipRepository.deleteById_TraineeIdAndId_TrainerId(traineeId, trainerId);
        if (rowsDeleted == 0) throw new RelationshipNotFoundException("Relationship not found between trainee and trainer");
    }

    @Override
    @Transactional(readOnly = true)
    public boolean exists(String traineeId, String trainerId) {
        validateId(traineeId, "traineeId");
        validateId(trainerId, "trainerId");
        return traineeTrainerRelationshipRepository.existsById_TraineeIdAndId_TrainerId(traineeId, trainerId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> getTrainersForTrainee(String traineeId) {
        validateId(traineeId, "traineeId");
        return traineeTrainerRelationshipRepository.findTrainerIdsByTraineeId(traineeId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> getTraineesForTrainer(String trainerId) {
        validateId(trainerId, "trainerId");
        return traineeTrainerRelationshipRepository.findTraineeIdsByTrainerId(trainerId);
    }

    private void validateId(String id, String paramName) {
        if (!StringUtils.hasText(id)) {
            throw new IllegalArgumentException(paramName + " must not be null or blank");
        }
    }
}
