package com.shyn9yskhan.trainer_workload_service.service;

import com.shyn9yskhan.trainer_workload_service.document.MonthSummaryDocument;
import com.shyn9yskhan.trainer_workload_service.document.TrainerTrainingSummaryDocument;
import com.shyn9yskhan.trainer_workload_service.document.YearDocument;
import com.shyn9yskhan.trainer_workload_service.domain.MonthSummary;
import com.shyn9yskhan.trainer_workload_service.domain.TrainerTrainingSummary;
import com.shyn9yskhan.trainer_workload_service.domain.Year;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DocumentMapper {
    public TrainerTrainingSummary toDomain(TrainerTrainingSummaryDocument trainerTrainingSummaryDocument) {
        String trainerId = trainerTrainingSummaryDocument.getTrainerId();
        List<YearDocument> yearDocuments = trainerTrainingSummaryDocument.getYears();

        List<Year> years = new ArrayList<>();
        for (YearDocument yearDocument : yearDocuments) {
            years.add(yearToDomain(yearDocument));
        }

        return new TrainerTrainingSummary(trainerId, years);
    }

    public TrainerTrainingSummaryDocument toDocument(TrainerTrainingSummary trainerTrainingSummary) {
        String trainerId = trainerTrainingSummary.getTrainerId();
        List<Year> years = trainerTrainingSummary.getYears();

        List<YearDocument> yearDocuments = new ArrayList<>();
        for (Year year : years) {
            yearDocuments.add(yearToDocument(year));
        }

        return new TrainerTrainingSummaryDocument(trainerId, yearDocuments);
    }

    public Year yearToDomain(YearDocument yearDocument) {
        Year year = new Year(yearDocument.getYear());
        for (MonthSummaryDocument monthSummaryDocument : yearDocument.getMonths()) {
            year.addToMonth(
                    monthSummaryDocument.getMonth(),
                    monthSummaryDocument.getTrainingsSummaryDuration()
            );
        }
        return year;
    }

    public YearDocument yearToDocument(Year year) {
        YearDocument yearDocument = new YearDocument();
        yearDocument.setYear(year.getYear());

        List<MonthSummaryDocument> monthSummaryDocuments = new ArrayList<>();
        for (MonthSummary monthSummary : year.getMonths()) {
            monthSummaryDocuments.add(monthToDocument(monthSummary));
        }

        yearDocument.setMonths(monthSummaryDocuments);
        return yearDocument;
    }

    public MonthSummary monthToDomain(MonthSummaryDocument monthSummaryDocument) {
        return new MonthSummary(monthSummaryDocument.getMonth(), monthSummaryDocument.getTrainingsSummaryDuration());
    }

    public MonthSummaryDocument monthToDocument(MonthSummary monthSummary) {
        return new MonthSummaryDocument(monthSummary.getMonth(), monthSummary.getTrainingsSummaryDuration());
    }
}
