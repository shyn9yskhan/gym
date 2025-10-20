package com.shyn9yskhan.trainer_workload_service.dto;

import com.shyn9yskhan.trainer_workload_service.domain.WorkloadAction;

import java.time.LocalDate;

public record WorkloadEventRequest(
        String trainerUsername,
        String trainerFirstname,
        String trainerLastname,
        boolean isActive,
        LocalDate trainingDate,
        int trainingDurationMinutes,
        WorkloadAction action
) {}
