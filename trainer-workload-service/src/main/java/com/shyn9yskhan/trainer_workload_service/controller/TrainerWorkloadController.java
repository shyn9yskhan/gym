package com.shyn9yskhan.trainer_workload_service.controller;

import com.shyn9yskhan.trainer_workload_service.dto.TrainerTrainingSummaryDto;
import com.shyn9yskhan.trainer_workload_service.dto.WorkloadEventRequest;
import com.shyn9yskhan.trainer_workload_service.service.TrainerWorkloadService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/trainer-workload")
public class TrainerWorkloadController {
    private final TrainerWorkloadService trainerWorkloadService;

    public TrainerWorkloadController(TrainerWorkloadService trainerWorkloadService) {
        this.trainerWorkloadService = trainerWorkloadService;
    }

    @PostMapping("/events")
    public ResponseEntity<Void> acceptWorkloadEvent(@RequestBody WorkloadEventRequest workloadEventRequest) {
        trainerWorkloadService.acceptWorkloadEvent(workloadEventRequest);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{trainerId}/summary")
    public ResponseEntity<TrainerTrainingSummaryDto> getTrainerSummary(@PathVariable String trainerId) {
        return ResponseEntity.ok(trainerWorkloadService.getTrainerSummary(trainerId));
    }
}
