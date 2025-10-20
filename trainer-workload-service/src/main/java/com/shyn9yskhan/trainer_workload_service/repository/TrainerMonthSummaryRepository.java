package com.shyn9yskhan.trainer_workload_service.repository;

import com.shyn9yskhan.trainer_workload_service.entity.TrainerMonthSummaryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TrainerMonthSummaryRepository extends JpaRepository<TrainerMonthSummaryEntity, String> {
    Optional<TrainerMonthSummaryEntity> findByTrainerUsernameAndYearAndMonth(String trainerUsername, int year, int month);
    List<TrainerMonthSummaryEntity> findByTrainerUsername(String trainerUsername);
}
