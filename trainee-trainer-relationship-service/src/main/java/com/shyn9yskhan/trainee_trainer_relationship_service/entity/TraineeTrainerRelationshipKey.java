package com.shyn9yskhan.trainee_trainer_relationship_service.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class TraineeTrainerRelationshipKey implements Serializable {
    @Column(name = "trainee_id", updatable = false, nullable = false)
    private String traineeId;

    @Column(name = "trainer_id", updatable = false, nullable = false)
    private String trainerId;

    public TraineeTrainerRelationshipKey() {
    }

    public TraineeTrainerRelationshipKey(String traineeId, String trainerId) {
        this.traineeId = traineeId;
        this.trainerId = trainerId;
    }

    public String getTraineeId() {
        return traineeId;
    }

    public void setTraineeId(String traineeId) {
        this.traineeId = traineeId;
    }

    public String getTrainerId() {
        return trainerId;
    }

    public void setTrainerId(String trainerId) {
        this.trainerId = trainerId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TraineeTrainerRelationshipKey that = (TraineeTrainerRelationshipKey) o;
        return traineeId.equals(that.traineeId) && trainerId.equals(that.trainerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(traineeId, trainerId);
    }
}
