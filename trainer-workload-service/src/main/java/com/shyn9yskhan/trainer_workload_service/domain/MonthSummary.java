package com.shyn9yskhan.trainer_workload_service.domain;

public class MonthSummary {
    private int month;
    private int trainingsSummaryDuration;

    public MonthSummary() {
    }

    public MonthSummary(int month, int trainingsSummaryDuration) {
        validateMonthNumber(month);
        validateTrainingDuration(trainingsSummaryDuration);
        this.month = month;
        this.trainingsSummaryDuration = trainingsSummaryDuration;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        validateMonthNumber(month);
        this.month = month;
    }

    public int getTrainingsSummaryDuration() {
        return trainingsSummaryDuration;
    }

    public void setTrainingsSummaryDuration(int trainingsSummaryDuration) {
        validateTrainingDuration(trainingsSummaryDuration);
        this.trainingsSummaryDuration = trainingsSummaryDuration;
    }

    public void addToTrainingsSummaryDuration(int trainingDuration) {
        validateTrainingDuration(trainingDuration);
        trainingsSummaryDuration += trainingDuration;
    }

    public void removeFromTrainingsSummaryDuration(int trainingDuration) {
        validateTrainingDuration(trainingDuration);
        if (trainingDuration > trainingsSummaryDuration) throw new IllegalArgumentException("Training duration cannot be greater than trainings summary duration");
        trainingsSummaryDuration -= trainingDuration;
    }

    private void validateMonthNumber(int month) {
        if (month < 1 || month > 12) throw new IllegalArgumentException(
                String.format("Month must be between 1 and 12, got: %d", month)
        );
    }

    private void validateTrainingDuration(int duration) {
        if (duration < 0) throw new IllegalArgumentException(
                String.format("training duration cannot be negative: %d", duration)
        );
    }
}
