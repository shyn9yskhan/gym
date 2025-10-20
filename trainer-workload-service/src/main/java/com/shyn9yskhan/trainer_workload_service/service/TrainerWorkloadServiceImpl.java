package com.shyn9yskhan.trainer_workload_service.service;

import com.shyn9yskhan.trainer_workload_service.domain.WorkloadAction;
import com.shyn9yskhan.trainer_workload_service.domain.MonthSummary;
import com.shyn9yskhan.trainer_workload_service.dto.TrainerMonthlySummaryResponse;
import com.shyn9yskhan.trainer_workload_service.domain.YearSummary;
import com.shyn9yskhan.trainer_workload_service.dto.WorkloadEventRequest;
import com.shyn9yskhan.trainer_workload_service.entity.TrainerEntity;
import com.shyn9yskhan.trainer_workload_service.entity.TrainerMonthSummaryEntity;
import com.shyn9yskhan.trainer_workload_service.repository.TrainerMonthSummaryRepository;
import com.shyn9yskhan.trainer_workload_service.repository.TrainerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TrainerWorkloadServiceImpl implements TrainerWorkloadService {
    private final TrainerRepository trainerRepository;
    private final TrainerMonthSummaryRepository trainerMonthSummaryRepository;

    public TrainerWorkloadServiceImpl(TrainerRepository trainerRepository, TrainerMonthSummaryRepository trainerMonthSummaryRepository) {
        this.trainerRepository = trainerRepository;
        this.trainerMonthSummaryRepository = trainerMonthSummaryRepository;
    }

    @Override
    @Transactional
    public void acceptWorkloadEvent(WorkloadEventRequest workloadEventRequest) {
        if (workloadEventRequest == null) {return;}

        WorkloadAction action = workloadEventRequest.action();
        String trainerUsername = workloadEventRequest.trainerUsername();
        LocalDate trainingDate = workloadEventRequest.trainingDate();
        int trainingDurationMinutes = workloadEventRequest.trainingDurationMinutes();

        if (trainerUsername == null || trainerUsername.isBlank() || trainingDate == null) {return;}

        int year = trainingDate.getYear();
        int month = trainingDate.getMonthValue();

        upsertTrainerMetadataIfNeeded(trainerUsername,
                workloadEventRequest.trainerFirstname(),
                workloadEventRequest.trainerLastname(),
                workloadEventRequest.isActive());

        switch (action) {
            case ADD -> handleAddEvent(trainerUsername, year, month, trainingDurationMinutes);
            case DELETE -> handleDeleteEvent(trainerUsername, year, month, trainingDurationMinutes);
            default -> {}
        }
    }

    private void upsertTrainerMetadataIfNeeded(String trainerUsername, String firstName, String lastName, boolean isActive) {
        Optional<TrainerEntity> trainerOpt = trainerRepository.findByTrainerUsername(trainerUsername);
        if (trainerOpt.isPresent()) {
            TrainerEntity existing = trainerOpt.get();
            boolean changed = false;
            if (firstName != null && !firstName.equals(existing.getTrainerFirstname())) {
                existing.setTrainerFirstname(firstName);
                changed = true;
            }
            if (lastName != null && !lastName.equals(existing.getTrainerLastname())) {
                existing.setTrainerLastname(lastName);
                changed = true;
            }
            if (existing.isActive() != isActive) {
                existing.setActive(isActive);
                changed = true;
            }
            if (changed) {
                trainerRepository.save(existing);
            }
        } else {
            TrainerEntity newTrainer = new TrainerEntity(trainerUsername, firstName, lastName, isActive);
            trainerRepository.save(newTrainer);
        }
    }

    private void handleAddEvent(String trainerUsername, int year, int month, int durationMinutes) {
        Optional<TrainerMonthSummaryEntity> summaryOpt =
                trainerMonthSummaryRepository.findByTrainerUsernameAndYearAndMonth(trainerUsername, year, month);

        if (summaryOpt.isPresent()) {
            TrainerMonthSummaryEntity summary = summaryOpt.get();
            long newTotal = summary.getTotalDurationMinutes() + (long) durationMinutes;
            summary.setTotalDurationMinutes(newTotal);
            trainerMonthSummaryRepository.save(summary);
        } else {
            TrainerMonthSummaryEntity created = new TrainerMonthSummaryEntity(
                    trainerUsername,
                    year,
                    month,
                    durationMinutes
            );
            trainerMonthSummaryRepository.save(created);
        }
    }

    private void handleDeleteEvent(String trainerUsername, int year, int month, int durationMinutes) {
        Optional<TrainerMonthSummaryEntity> summaryOpt =
                trainerMonthSummaryRepository.findByTrainerUsernameAndYearAndMonth(trainerUsername, year, month);

        if (summaryOpt.isEmpty()) {
            return;
        }

        TrainerMonthSummaryEntity summary = summaryOpt.get();
        long currentTotal = summary.getTotalDurationMinutes();
        long newTotal = currentTotal - (long) durationMinutes;

        if (newTotal > 0) {
            summary.setTotalDurationMinutes(newTotal);
            trainerMonthSummaryRepository.save(summary);
        } else {
            trainerMonthSummaryRepository.delete(summary);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public TrainerMonthlySummaryResponse getTrainerSummary(String username) {
        if (username == null || username.isBlank()) {
            return new TrainerMonthlySummaryResponse("", null, null, false, Collections.emptyList());
        }

        Optional<TrainerEntity> trainerOpt = trainerRepository.findByTrainerUsername(username);
        if (trainerOpt.isEmpty()) {
            return new TrainerMonthlySummaryResponse(username, null, null, false, Collections.emptyList());
        }

        TrainerEntity trainer = trainerOpt.get();

        List<TrainerMonthSummaryEntity> monthRows = trainerMonthSummaryRepository.findByTrainerUsername(username);

        Map<Integer, List<TrainerMonthSummaryEntity>> groupedByYear = monthRows.stream()
                .collect(Collectors.groupingBy(TrainerMonthSummaryEntity::getYear));

        List<YearSummary> years = groupedByYear.entrySet().stream()
                .map(entry -> {
                    int year = entry.getKey();
                    List<MonthSummary> months = entry.getValue().stream()
                            .sorted(Comparator.comparingInt(TrainerMonthSummaryEntity::getMonth))
                            .map(e -> new MonthSummary(e.getMonth(), (int) e.getTotalDurationMinutes()))
                            .collect(Collectors.toList());
                    return new YearSummary(year, months);
                })
                .sorted(Comparator.comparingInt(YearSummary::year))
                .collect(Collectors.toList());

        return new TrainerMonthlySummaryResponse(
                trainer.getTrainerUsername(),
                trainer.getTrainerFirstname(),
                trainer.getTrainerLastname(),
                trainer.isActive(),
                years
        );
    }
}
