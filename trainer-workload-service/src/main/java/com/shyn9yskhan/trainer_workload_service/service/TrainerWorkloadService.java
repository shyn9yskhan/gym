package com.shyn9yskhan.trainer_workload_service.service;

import com.shyn9yskhan.trainer_workload_service.dto.TrainerMonthlySummaryResponse;
import com.shyn9yskhan.trainer_workload_service.dto.WorkloadEventRequest;

public interface TrainerWorkloadService {
    void acceptWorkloadEvent(WorkloadEventRequest workloadEventRequest);
    TrainerMonthlySummaryResponse getTrainerSummary(String username);
}
