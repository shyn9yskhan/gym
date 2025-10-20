package com.shyn9yskhan.trainer_service.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.UuidGenerator;

@Entity
@Table(name = "trainer")
public class TrainerEntity {
    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "id", updatable = false, nullable = false)
    private String id;

    @Column(name = "specialization")
    private String specialization;

    @Column(name = "user_id", updatable = false, nullable = false)
    private String userId;

    public TrainerEntity() {
    }

    public TrainerEntity(String id, String specialization, String userId) {
        this.id = id;
        this.specialization = specialization;
        this.userId = userId;
    }

    public TrainerEntity(String specialization, String userId) {
        this.specialization = specialization;
        this.userId = userId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
