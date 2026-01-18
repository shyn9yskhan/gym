package com.shyn9yskhan.training_service.service;

import com.shyn9yskhan.training_service.dto.TrainingEvent;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class MessageSender {

    private final SqsTemplate sqsTemplate;
    private final String queueUrl;

    public MessageSender(SqsTemplate sqsTemplate, @Value("${spring.cloud.aws.sqs.endpoint}") String queueUrl) {
        this.sqsTemplate = sqsTemplate;
        this.queueUrl = queueUrl;
    }

    public void sendTrainingUpdate(TrainingEvent trainingEvent) {
        sqsTemplate.send(to -> to.queue(queueUrl).payload(trainingEvent));
    }
}
