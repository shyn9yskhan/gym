package com.shyn9yskhan.trainee_trainer_relationship_service.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "trainee_trainer_relationship")
public class TraineeTrainerRelationshipEntity {
    @EmbeddedId
    private TraineeTrainerRelationshipKey id;

    public TraineeTrainerRelationshipEntity() {
    }

    public TraineeTrainerRelationshipEntity(TraineeTrainerRelationshipKey id) {
        this.id = id;
    }

    public TraineeTrainerRelationshipKey getId() {
        return id;
    }

    public void setId(TraineeTrainerRelationshipKey id) {
        this.id = id;
    }
}
