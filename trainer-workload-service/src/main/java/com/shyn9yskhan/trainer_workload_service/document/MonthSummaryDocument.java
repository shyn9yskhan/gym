package com.shyn9yskhan.trainer_workload_service.document;

public class MonthSummaryDocument {
    private int month;
    private int trainingsSummaryDuration;

    public MonthSummaryDocument() {
    }

    public MonthSummaryDocument(int month, int trainingsSummaryDuration) {
        this.month = month;
        this.trainingsSummaryDuration = trainingsSummaryDuration;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getTrainingsSummaryDuration() {
        return trainingsSummaryDuration;
    }

    public void setTrainingsSummaryDuration(int trainingsSummaryDuration) {
        this.trainingsSummaryDuration = trainingsSummaryDuration;
    }
}
