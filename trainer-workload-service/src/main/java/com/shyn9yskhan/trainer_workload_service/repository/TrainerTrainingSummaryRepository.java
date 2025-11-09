package com.shyn9yskhan.trainer_workload_service.repository;

import com.shyn9yskhan.trainer_workload_service.document.TrainerTrainingSummaryDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TrainerTrainingSummaryRepository extends MongoRepository<TrainerTrainingSummaryDocument, String> {
}
