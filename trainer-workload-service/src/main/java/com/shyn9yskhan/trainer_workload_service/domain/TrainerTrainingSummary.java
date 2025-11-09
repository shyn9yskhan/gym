package com.shyn9yskhan.trainer_workload_service.domain;

import java.util.List;

public class TrainerTrainingSummary {
    private String trainerId;
    private List<Year> years;

    public TrainerTrainingSummary() {
    }

    public TrainerTrainingSummary(String trainerId, List<Year> years) {
        this.trainerId = trainerId;
        this.years = years;
    }

    public String getTrainerId() {
        return trainerId;
    }

    public void setTrainerId(String trainerId) {
        this.trainerId = trainerId;
    }

    public List<Year> getYears() {
        return years;
    }

    public void setYears(List<Year> years) {
        this.years = years;
    }

    public void addYear(Year year) {
        this.years.add(year);
    }

    public void removeYear(int year) {
        years.removeIf(y -> y.getYear() == year);
    }

    public Year getYear(int year) {
        Year yearObj = null;
        for (Year y : years) {
            if (y.getYear() == year) yearObj = y;
        }
        return yearObj;
    }
}
