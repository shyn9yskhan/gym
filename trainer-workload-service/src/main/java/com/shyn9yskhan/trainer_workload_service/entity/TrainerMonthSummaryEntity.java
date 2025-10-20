package com.shyn9yskhan.trainer_workload_service.entity;

public class TrainerMonthSummaryEntity {
    private Long id;
    private String trainerUsername;
    private int year;
    private int month;
    private long totalDurationMinutes;

    public TrainerMonthSummaryEntity() {
    }

    public TrainerMonthSummaryEntity(String trainerUsername, int year, int month, long totalDurationMinutes) {
        this.trainerUsername = trainerUsername;
        this.year = year;
        this.month = month;
        this.totalDurationMinutes = totalDurationMinutes;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTrainerUsername() {
        return trainerUsername;
    }

    public void setTrainerUsername(String trainerUsername) {
        this.trainerUsername = trainerUsername;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public long getTotalDurationMinutes() {
        return totalDurationMinutes;
    }

    public void setTotalDurationMinutes(long totalDurationMinutes) {
        this.totalDurationMinutes = totalDurationMinutes;
    }
}
