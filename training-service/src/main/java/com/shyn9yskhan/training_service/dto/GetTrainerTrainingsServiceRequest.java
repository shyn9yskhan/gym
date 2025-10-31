package com.shyn9yskhan.training_service.dto;

import java.io.Serializable;

public class GetTrainerTrainingsServiceRequest implements Serializable {
    private String trainerId;
    public GetTrainerTrainingsServiceRequest() {}
    public GetTrainerTrainingsServiceRequest(String trainerId) { this.trainerId = trainerId; }
    public String getTrainerId() { return trainerId; }
    public void setTrainerId(String trainerId) { this.trainerId = trainerId; }
}
