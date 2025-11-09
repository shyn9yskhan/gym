package com.shyn9yskhan.trainer_workload_service.domain;

public class MonthSummary {
    private int month;
    private int trainingsSummaryDuration;

    public MonthSummary() {
    }

    public MonthSummary(int month, int trainingsSummaryDuration) {
        this.month = month;
        this.trainingsSummaryDuration = trainingsSummaryDuration;
    }

    public int getMonth() {
        return month;
    }

    public int getTrainingsSummaryDuration() {
        return trainingsSummaryDuration;
    }

    public void setTrainingsSummaryDuration(int trainingsSummaryDuration) {
        this.trainingsSummaryDuration = trainingsSummaryDuration;
    }

    public void addToTrainingsSummaryDuration(int trainingDuration) {
        if (trainingDuration < 0) throw new IllegalArgumentException("training duration cannot be negative");
        trainingsSummaryDuration += trainingDuration;
    }

    public void removeFromTrainingsSummaryDuration(int trainingDuration) {
        if (trainingDuration < 0) throw new IllegalArgumentException("training duration cannot be negative");
        if (trainingDuration > trainingsSummaryDuration) throw new IllegalArgumentException("Training duration cannot be greater than trainings summary duration");
        trainingsSummaryDuration -= trainingDuration;
    }
}
