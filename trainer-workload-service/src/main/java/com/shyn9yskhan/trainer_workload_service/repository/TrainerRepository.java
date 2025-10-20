package com.shyn9yskhan.trainer_workload_service.repository;

import com.shyn9yskhan.trainer_workload_service.entity.TrainerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TrainerRepository extends JpaRepository<TrainerEntity, String> {
    boolean existsByTrainerUsername(String trainerUsername);
    Optional<TrainerEntity> findByTrainerUsername(String trainerUsername);
}
