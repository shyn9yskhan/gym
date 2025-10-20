package com.shyn9yskhan.trainee_trainer_relationship_service.repository;

import com.shyn9yskhan.trainee_trainer_relationship_service.entity.TraineeTrainerRelationshipEntity;
import com.shyn9yskhan.trainee_trainer_relationship_service.entity.TraineeTrainerRelationshipKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TraineeTrainerRelationshipRepository extends JpaRepository<TraineeTrainerRelationshipEntity, TraineeTrainerRelationshipKey> {
    boolean existsById_TraineeIdAndId_TrainerId(String traineeId, String trainerId);

    @Modifying
    @Query("DELETE FROM TraineeTrainerRelationshipEntity t WHERE t.id.traineeId = :traineeId AND t.id.trainerId = :trainerId")
    int deleteById_TraineeIdAndId_TrainerId(@Param("traineeId") String traineeId, @Param("trainerId") String trainerId);

    @Query("SELECT t.id.trainerId FROM TraineeTrainerRelationshipEntity t WHERE t.id.traineeId = :traineeId")
    List<String> findTrainerIdsByTraineeId(@Param("traineeId") String traineeId);

    @Query("SELECT t.id.traineeId FROM TraineeTrainerRelationshipEntity t WHERE t.id.trainerId = :trainerId")
    List<String> findTraineeIdsByTrainerId(@Param("trainerId") String trainerId);
}
