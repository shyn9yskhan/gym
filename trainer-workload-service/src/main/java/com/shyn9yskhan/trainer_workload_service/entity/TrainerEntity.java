package com.shyn9yskhan.trainer_workload_service.entity;

public class TrainerEntity {
    private String trainerUsername;
    private String trainerFirstname;
    private String trainerLastname;
    private boolean isActive;

    public TrainerEntity() {
    }

    public TrainerEntity(String trainerUsername, String trainerFirstname, String trainerLastname, boolean isActive) {
        this.trainerUsername = trainerUsername;
        this.trainerFirstname = trainerFirstname;
        this.trainerLastname = trainerLastname;
        this.isActive = isActive;
    }

    public String getTrainerUsername() {
        return trainerUsername;
    }

    public void setTrainerUsername(String trainerUsername) {
        this.trainerUsername = trainerUsername;
    }

    public String getTrainerFirstname() {
        return trainerFirstname;
    }

    public void setTrainerFirstname(String trainerFirstname) {
        this.trainerFirstname = trainerFirstname;
    }

    public String getTrainerLastname() {
        return trainerLastname;
    }

    public void setTrainerLastname(String trainerLastname) {
        this.trainerLastname = trainerLastname;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
