package com.shyn9yskhan.training_service.dto;

import java.io.Serializable;

public class GetTraineeTrainingsServiceRequest implements Serializable {
    private String traineeId;
    public GetTraineeTrainingsServiceRequest() {}
    public GetTraineeTrainingsServiceRequest(String traineeId) { this.traineeId = traineeId; }
    public String getTraineeId() { return traineeId; }
    public void setTraineeId(String traineeId) { this.traineeId = traineeId; }
}
