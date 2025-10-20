package com.shyn9yskhan.trainer_workload_service.dto;

import com.shyn9yskhan.trainer_workload_service.domain.YearSummary;

import java.util.List;

public record TrainerMonthlySummaryResponse(
        String trainerUsername,
        String trainerFirstname,
        String trainerLastname,
        boolean isActive,
        List<YearSummary> years
) {}
