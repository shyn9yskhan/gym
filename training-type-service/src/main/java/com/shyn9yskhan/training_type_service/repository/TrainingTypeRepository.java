package com.shyn9yskhan.training_type_service.repository;

import com.shyn9yskhan.training_type_service.entity.TrainingTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrainingTypeRepository extends JpaRepository<TrainingTypeEntity, String> {
}
