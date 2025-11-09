package com.shyn9yskhan.trainer_workload_service.service;

import com.shyn9yskhan.trainer_workload_service.domain.MonthSummary;
import com.shyn9yskhan.trainer_workload_service.domain.TrainerTrainingSummary;
import com.shyn9yskhan.trainer_workload_service.domain.Year;
import com.shyn9yskhan.trainer_workload_service.dto.MonthSummaryDto;
import com.shyn9yskhan.trainer_workload_service.dto.TrainerTrainingSummaryDto;
import com.shyn9yskhan.trainer_workload_service.dto.YearDto;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DtoMapper {
    public TrainerTrainingSummaryDto toDto(TrainerTrainingSummary trainerTrainingSummary) {
        List<YearDto> yearDtos = new ArrayList<>();
        for (Year year : trainerTrainingSummary.getYears()) {
            yearDtos.add(yearToDto(year));
        }
        return new TrainerTrainingSummaryDto(yearDtos);
    }

    public YearDto yearToDto(Year year) {
        List<MonthSummaryDto> monthSummaryDtos = new ArrayList<>();
        for (MonthSummary monthSummary : year.getMonths()) {
            monthSummaryDtos.add(monthToDto(monthSummary));
        }
        return new YearDto(year.getYear(), monthSummaryDtos);
    }

    public MonthSummaryDto monthToDto(MonthSummary month) {
        return new MonthSummaryDto(month.getMonth(), month.getTrainingsSummaryDuration());
    }
}
