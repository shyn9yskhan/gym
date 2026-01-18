package com.shyn9yskhan.trainer_workload_service.document;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

import java.util.List;

@DynamoDbBean
public class TrainerTrainingSummaryDocument {
    private String trainerId;
    private List<YearDocument> years;

    public TrainerTrainingSummaryDocument() {
    }

    public TrainerTrainingSummaryDocument(String trainerId, List<YearDocument> years) {
        this.trainerId = trainerId;
        this.years = years;
    }

    @DynamoDbPartitionKey
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
