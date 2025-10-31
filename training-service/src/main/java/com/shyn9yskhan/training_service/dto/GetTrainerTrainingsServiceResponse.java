package com.shyn9yskhan.training_service.dto;

import java.io.Serializable;
import java.util.List;

public class GetTrainerTrainingsServiceResponse implements Serializable {
    private List<TrainingDto> trainings;
    public GetTrainerTrainingsServiceResponse() {}
    public GetTrainerTrainingsServiceResponse(List<TrainingDto> trainings) { this.trainings = trainings; }
    public List<TrainingDto> getTrainings() { return trainings; }
    public void setTrainings(List<TrainingDto> trainings) { this.trainings = trainings; }
}
