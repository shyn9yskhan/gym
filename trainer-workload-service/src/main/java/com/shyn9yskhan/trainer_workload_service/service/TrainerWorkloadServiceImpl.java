package com.shyn9yskhan.trainer_workload_service.service;

import com.shyn9yskhan.trainer_workload_service.document.TrainerTrainingSummaryDocument;
import com.shyn9yskhan.trainer_workload_service.domain.MonthSummary;
import com.shyn9yskhan.trainer_workload_service.domain.TrainerTrainingSummary;
import com.shyn9yskhan.trainer_workload_service.domain.Year;
import com.shyn9yskhan.trainer_workload_service.dto.TrainerTrainingSummaryDto;
import com.shyn9yskhan.trainer_workload_service.domain.WorkloadAction;
import com.shyn9yskhan.trainer_workload_service.dto.WorkloadEventRequest;
import com.shyn9yskhan.trainer_workload_service.repository.TrainerTrainingSummaryRepository;
import jakarta.ws.rs.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TrainerWorkloadServiceImpl implements TrainerWorkloadService {
    private final TrainerTrainingSummaryRepository trainerTrainingSummaryRepository;
    private final DocumentMapper documentMapper;
    private final DtoMapper dtoMapper;

    public TrainerWorkloadServiceImpl(TrainerTrainingSummaryRepository trainerTrainingSummaryRepository, DocumentMapper documentMapper, DtoMapper dtoMapper) {
        this.trainerTrainingSummaryRepository = trainerTrainingSummaryRepository;
        this.documentMapper = documentMapper;
        this.dtoMapper = dtoMapper;
    }

    @Override
    @Transactional
    public void acceptWorkloadEvent(WorkloadEventRequest workloadEventRequest) {
        String trainerId = workloadEventRequest.trainerId();
        int year = workloadEventRequest.trainingDate().getYear();
        int month = workloadEventRequest.trainingDate().getMonthValue();
        int trainingDuration = workloadEventRequest.trainingDurationMinutes();
        WorkloadAction workloadAction = workloadEventRequest.action();

        Optional<TrainerTrainingSummaryDocument> optionalTrainerTrainingSummaryDocument = trainerTrainingSummaryRepository.findById(trainerId);

        switch (workloadAction) {
            case ADD -> {
                if (optionalTrainerTrainingSummaryDocument.isPresent()) {
                    TrainerTrainingSummaryDocument trainerTrainingSummaryDocument = optionalTrainerTrainingSummaryDocument.get();
                    TrainerTrainingSummary trainerTrainingSummary = documentMapper.toDomain(trainerTrainingSummaryDocument);
                    Year yearDomain = trainerTrainingSummary.getYear(year);
                    MonthSummary monthSummary = yearDomain.getMonth(month);
                    monthSummary.addToTrainingsSummaryDuration(trainingDuration);

                    TrainerTrainingSummaryDocument updatedTrainerTrainingSummaryDocument = documentMapper.toDocument(trainerTrainingSummary);
                    trainerTrainingSummaryRepository.save(updatedTrainerTrainingSummaryDocument);
                }
                else {
                    List<Year> years = new ArrayList<>();
                    Year yearDomain = new Year(year);
                    MonthSummary monthSummary = new MonthSummary(month, trainingDuration);
                    yearDomain.addMonth(monthSummary);
                    years.add(yearDomain);

                    TrainerTrainingSummary trainerTrainingSummary = new TrainerTrainingSummary(trainerId, years);

                    TrainerTrainingSummaryDocument trainerTrainingSummaryDocument = documentMapper.toDocument(trainerTrainingSummary);
                    trainerTrainingSummaryRepository.save(trainerTrainingSummaryDocument);
                }
            }

            case DELETE -> {
                if (optionalTrainerTrainingSummaryDocument.isPresent()) {
                    TrainerTrainingSummaryDocument trainerTrainingSummaryDocument = optionalTrainerTrainingSummaryDocument.get();
                    TrainerTrainingSummary trainerTrainingSummary = documentMapper.toDomain(trainerTrainingSummaryDocument);
                    Year yearDomain = trainerTrainingSummary.getYear(year);
                    MonthSummary monthSummary = yearDomain.getMonth(month);
                    monthSummary.removeFromTrainingsSummaryDuration(trainingDuration);

                    TrainerTrainingSummaryDocument updatedTrainerTrainingSummaryDocument = documentMapper.toDocument(trainerTrainingSummary);
                    trainerTrainingSummaryRepository.save(updatedTrainerTrainingSummaryDocument);
                }
                else throw new NotFoundException("Trainer trainings summary not found");
            }
        }
    }

    @Override
    @Transactional(readOnly = true)
    public TrainerTrainingSummaryDto getTrainerSummary(String trainerId) {
        Optional<TrainerTrainingSummaryDocument> optionalTrainerTrainingSummaryDocument = trainerTrainingSummaryRepository.findById(trainerId);
        if (optionalTrainerTrainingSummaryDocument.isPresent()) {
            TrainerTrainingSummaryDocument trainerTrainingSummaryDocument = optionalTrainerTrainingSummaryDocument.get();
            TrainerTrainingSummary trainerTrainingSummary = documentMapper.toDomain(trainerTrainingSummaryDocument);
            return dtoMapper.toDto(trainerTrainingSummary);
        }
        else throw new NotFoundException("Trainer trainings summary not found");
    }
}
