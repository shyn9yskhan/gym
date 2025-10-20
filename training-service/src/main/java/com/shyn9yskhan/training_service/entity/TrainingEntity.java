package com.shyn9yskhan.training_service.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDate;

@Entity
@Table(name = "training")
public class TrainingEntity {
    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "id", updatable = false, nullable = false)
    private String id;

    @Column(name = "trainee_id", nullable = false)
    private String traineeId;

    @Column(name = "trainer_id", nullable = false)
    private String trainerId;

    @Column(name = "training_name", nullable = false)
    private String trainingName;

    @Column(name = "training_type_id", nullable = false)
    private String trainingTypeId;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "duration", nullable = false)
    private int duration;

    public TrainingEntity() {
    }

    public TrainingEntity(String id, String traineeId, String trainerId, String trainingName, String trainingTypeId, LocalDate date, int trainingDuration) {
        this.id = id;
        this.traineeId = traineeId;
        this.trainerId = trainerId;
        this.trainingName = trainingName;
        this.trainingTypeId = trainingTypeId;
        this.date = date;
        this.duration = trainingDuration;
    }

    public TrainingEntity(String traineeId, String trainerId, String trainingName, String trainingTypeId, LocalDate date, int duration) {
        this.traineeId = traineeId;
        this.trainerId = trainerId;
        this.trainingName = trainingName;
        this.trainingTypeId = trainingTypeId;
        this.date = date;
        this.duration = duration;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getTrainingName() {
        return trainingName;
    }

    public void setTrainingName(String trainingName) {
        this.trainingName = trainingName;
    }

    public String getTrainingTypeId() {
        return trainingTypeId;
    }

    public void setTrainingTypeId(String trainingTypeId) {
        this.trainingTypeId = trainingTypeId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}
