package com.shyn9yskhan.trainer_service.repository;

import com.shyn9yskhan.trainer_service.entity.TrainerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TrainerRepository extends JpaRepository<TrainerEntity, String> {
    Optional<TrainerEntity> findByUserId(String userId);
    List<TrainerEntity> findByIdIn(List<String> ids);
    List<TrainerEntity> findByUserIdIn(List<String> userIds);

    @Query("SELECT t.id FROM TrainerEntity t WHERE t.userId = :userId")
    Optional<String> findIdByUserId(@Param("userId") String userId);

    @Query("SELECT t.specialization FROM TrainerEntity t WHERE t.id = :trainerId")
    Optional<String> findTrainingTypeIdByTrainerId(@Param("trainerId") String trainerId);

    @Modifying
    @Query("DELETE FROM TrainerEntity t WHERE t.id = :trainerId")
    int deleteTrainerById(@Param("trainerId") String trainerId);

    @Modifying
    @Query("DELETE FROM TrainerEntity t WHERE t.userId = :userId")
    int deleteTrainerByUserIdQuery(@Param("userId") String userId);

    @Modifying
    @Query("UPDATE TrainerEntity t SET t.specialization = :specialization WHERE t.id = :trainerId")
    int updateTrainer(@Param("trainerId") String trainerId,
                      @Param("specialization") String specialization);

    @Modifying
    @Query("UPDATE TrainerEntity t SET t.specialization = :specialization WHERE t.userId = :userId")
    int updateTrainerByUserId(@Param("userId") String userId,
                              @Param("specialization") String specialization);
}
