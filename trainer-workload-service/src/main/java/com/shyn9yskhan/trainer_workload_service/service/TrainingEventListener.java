package com.shyn9yskhan.trainer_workload_service.service;

import com.shyn9yskhan.trainer_workload_service.dto.TrainingEvent;
import com.shyn9yskhan.trainer_workload_service.dto.WorkloadEventRequest;
import io.awspring.cloud.sqs.annotation.SqsListener;
import org.springframework.stereotype.Component;

@Component
public class TrainingEventListener {
    private final TrainerWorkloadService trainerWorkloadService;

    public TrainingEventListener(TrainerWorkloadService trainerWorkloadService) {
        this.trainerWorkloadService = trainerWorkloadService;
    }

    @SqsListener("${spring.cloud.aws.sqs.endpoint}")
    public void receiveTrainingEvent(TrainingEvent trainingEvent) {
        WorkloadEventRequest workloadEventRequest = new WorkloadEventRequest(
                trainingEvent.getTrainerId(),
                trainingEvent.getTrainingDate(),
                trainingEvent.getTrainingDurationMinutes(),
                trainingEvent.getAction()
        );
        trainerWorkloadService.acceptWorkloadEvent(workloadEventRequest);
    }
}
