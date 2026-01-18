package com.shyn9yskhan.trainer_workload_service.repository;

import com.shyn9yskhan.trainer_workload_service.document.TrainerTrainingSummaryDocument;
import io.awspring.cloud.dynamodb.DynamoDbTemplate;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class TrainerTrainingSummaryRepository {

    private final DynamoDbTemplate dynamoDbTemplate;

    public TrainerTrainingSummaryRepository(DynamoDbTemplate dynamoDbTemplate) {
        this.dynamoDbTemplate = dynamoDbTemplate;
    }

    public void save(TrainerTrainingSummaryDocument document) {
        dynamoDbTemplate.save(document);
    }

    public Optional<TrainerTrainingSummaryDocument> findById(String trainerId) {
        Key key = Key.builder()
                .partitionValue(trainerId)
                .build();

        return Optional.ofNullable(dynamoDbTemplate.load(key, TrainerTrainingSummaryDocument.class));
    }

    public void deleteById(String trainerId) {
        Key key = Key.builder().partitionValue(trainerId).build();
        dynamoDbTemplate.delete(key, TrainerTrainingSummaryDocument.class);
    }
}