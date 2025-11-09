package com.shyn9yskhan.trainer_workload_service.dto;

import java.util.List;

public record YearDto(
        int year,
        List<MonthSummaryDto> months
) {}
