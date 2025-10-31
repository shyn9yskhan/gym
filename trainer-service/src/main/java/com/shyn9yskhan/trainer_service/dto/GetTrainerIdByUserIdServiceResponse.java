package com.shyn9yskhan.trainer_service.dto;

import java.io.Serializable;

public class GetTrainerIdByUserIdServiceResponse implements Serializable {
    private String trainerId;
    public GetTrainerIdByUserIdServiceResponse() {}
    public GetTrainerIdByUserIdServiceResponse(String trainerId) { this.trainerId = trainerId; }
    public String getTrainerId() { return trainerId; }
    public void setTrainerId(String trainerId) { this.trainerId = trainerId; }
}
