package com.shyn9yskhan.trainer_workload_service.controller;

import com.shyn9yskhan.trainer_workload_service.dto.TrainerMonthlySummaryResponse;
import com.shyn9yskhan.trainer_workload_service.dto.WorkloadEventRequest;
import com.shyn9yskhan.trainer_workload_service.service.TrainerWorkloadService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/{username}/summary")
    public ResponseEntity<TrainerMonthlySummaryResponse> getTrainerSummary(@PathVariable String username) {
        return ResponseEntity.ok(trainerWorkloadService.getTrainerSummary(username));
    }
}
