package com.shyn9yskhan.training_service.repository;

import com.shyn9yskhan.training_service.entity.TrainingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrainingRepository extends JpaRepository<TrainingEntity, String> {
    List<TrainingEntity> findByTraineeId(String traineeId);
    List<TrainingEntity> findByTrainerId(String trainerId);
}
