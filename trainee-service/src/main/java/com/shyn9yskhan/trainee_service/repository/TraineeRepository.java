package com.shyn9yskhan.trainee_service.repository;

import com.shyn9yskhan.trainee_service.entity.TraineeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface TraineeRepository extends JpaRepository<TraineeEntity, String> {
    Optional<TraineeEntity> findByUserId(String userId);
    List<TraineeEntity> findByIdIn(List<String> ids);

    @Query("SELECT t.id FROM TraineeEntity t WHERE t.userId = :userId")
    Optional<String> findIdByUserId(@Param("userId") String userId);

    @Modifying
    @Query("DELETE FROM TraineeEntity t WHERE t.id = :traineeId")
    int deleteTraineeById(@Param("traineeId") String traineeId);

    @Modifying
    @Query("DELETE FROM TraineeEntity t WHERE t.userId = :userId")
    int deleteTraineeByUserIdQuery(@Param("traineeId") String userId);

    @Modifying
    @Query("UPDATE TraineeEntity t SET t.dateOfBirth = :dateOfBirth, t.address = :address WHERE t.id = :traineeId")
    int updateTrainee(@Param("traineeId") String traineeId,
                      @Param("dateOfBirth") LocalDate dateOfBirth,
                      @Param("address") String address);

    @Modifying
    @Query("UPDATE TraineeEntity t SET t.dateOfBirth = :dateOfBirth, t.address = :address WHERE t.userId = :userId")
    int updateTraineeByUserId(@Param("traineeId") String userId,
                              @Param("dateOfBirth") LocalDate dateOfBirth,
                              @Param("address") String address);
}
