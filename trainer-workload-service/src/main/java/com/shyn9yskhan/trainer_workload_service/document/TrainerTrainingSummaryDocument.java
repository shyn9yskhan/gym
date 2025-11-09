package com.shyn9yskhan.trainer_workload_service.document;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document("trainer_training_summary")
public class TrainerTrainingSummaryDocument {
    @Id
    private String trainerId;
    private List<YearDocument> years;

    public TrainerTrainingSummaryDocument() {
    }

    public TrainerTrainingSummaryDocument(String trainerId, List<YearDocument> years) {
        this.trainerId = trainerId;
        this.years = years;
    }

    public String getTrainerId() {
        return trainerId;
    }

    public void setTrainerId(String trainerId) {
        this.trainerId = trainerId;
    }

    public List<YearDocument> getYears() {
        return years;
    }

    public void setYears(List<YearDocument> years) {
        this.years = years;
    }
}
