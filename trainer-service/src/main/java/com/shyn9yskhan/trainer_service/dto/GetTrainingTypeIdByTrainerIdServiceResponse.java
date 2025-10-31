package com.shyn9yskhan.trainer_service.dto;

import java.io.Serializable;

public class GetTrainingTypeIdByTrainerIdServiceResponse implements Serializable {
    private String trainingTypeId;
    public GetTrainingTypeIdByTrainerIdServiceResponse() {}
    public GetTrainingTypeIdByTrainerIdServiceResponse(String trainingTypeId) { this.trainingTypeId = trainingTypeId; }
    public String getTrainingTypeId() { return trainingTypeId; }
    public void setTrainingTypeId(String trainingTypeId) { this.trainingTypeId = trainingTypeId; }
}
