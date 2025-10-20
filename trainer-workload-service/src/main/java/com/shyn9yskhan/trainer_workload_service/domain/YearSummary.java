package com.shyn9yskhan.trainer_workload_service.domain;

import java.util.List;

public record YearSummary(
        int year,
        List<MonthSummary> months
) {}
