package com.shyn9yskhan.trainer_workload_service.dto;

import com.shyn9yskhan.trainer_workload_service.domain.WorkloadAction;

import java.time.LocalDate;

public class TrainingEvent {
    private String trainerId;
    private LocalDate trainingDate;
    private int trainingDurationMinutes;
    private WorkloadAction action;

    public TrainingEvent() {
    }

    public TrainingEvent(String trainerId, LocalDate trainingDate, int trainingDurationMinutes, WorkloadAction action) {
        this.trainerId = trainerId;
        this.trainingDate = trainingDate;
        this.trainingDurationMinutes = trainingDurationMinutes;
        this.action = action;
    }

    public String getTrainerId() {
        return trainerId;
    }

    public void setTrainerId(String trainerId) {
        this.trainerId = trainerId;
    }

    public LocalDate getTrainingDate() {
        return trainingDate;
    }

    public void setTrainingDate(LocalDate trainingDate) {
        this.trainingDate = trainingDate;
    }

    public int getTrainingDurationMinutes() {
        return trainingDurationMinutes;
    }

    public void setTrainingDurationMinutes(int trainingDurationMinutes) {
        this.trainingDurationMinutes = trainingDurationMinutes;
    }

    public WorkloadAction getAction() {
        return action;
    }

    public void setAction(WorkloadAction action) {
        this.action = action;
    }
}
