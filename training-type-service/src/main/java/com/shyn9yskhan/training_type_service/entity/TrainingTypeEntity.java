package com.shyn9yskhan.training_type_service.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.UuidGenerator;

@Entity
@Table(name = "training_type")
public class TrainingTypeEntity {
    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "id", updatable = false, nullable = false)
    private String id;

    @Column(name = "training_type_name", nullable = false)
    private String trainingTypeName;

    public TrainingTypeEntity() {
    }

    public TrainingTypeEntity(String id, String trainingTypeName) {
        this.id = id;
        this.trainingTypeName = trainingTypeName;
    }

    public TrainingTypeEntity(String trainingTypeName) {
        this.trainingTypeName = trainingTypeName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTrainingTypeName() {
        return trainingTypeName;
    }

    public void setTrainingTypeName(String trainingTypeName) {
        this.trainingTypeName = trainingTypeName;
    }
}
