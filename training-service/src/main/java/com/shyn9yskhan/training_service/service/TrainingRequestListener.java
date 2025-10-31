package com.shyn9yskhan.training_service.service;

import com.shyn9yskhan.training_service.dto.*;
import com.shyn9yskhan.training_service.entity.TrainingEntity;
import com.shyn9yskhan.training_service.repository.TrainingRepository;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import jakarta.jms.Destination;
import jakarta.jms.JMSException;
import jakarta.jms.Message;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class TrainingRequestListener {

    private final TrainingRepository trainingRepository;
    private final JmsTemplate jmsTemplate;

    private static final String OPERATION_HEADER = "operation";
    private static final String OP_GET_TRAININGS_BY_TRAINEE = "GetTrainingsByTraineeId";
    private static final String OP_GET_TRAININGS_BY_TRAINER = "GetTrainingsByTrainerId";
    private static final String OP_CREATE_TRAINING = "CreateTraining";

    public TrainingRequestListener(TrainingRepository trainingRepository, JmsTemplate jmsTemplate) {
        this.trainingRepository = trainingRepository;
        this.jmsTemplate = jmsTemplate;
    }

    @JmsListener(destination = "training.request.queue", containerFactory = "jmsListenerContainerFactory")
    public void onMessage(Object payload, Message rawMessage) throws JMSException {
        String operation = null;
        try {
            operation = rawMessage.getStringProperty(OPERATION_HEADER);
        } catch (JMSException ignored) { }

        if (OP_GET_TRAININGS_BY_TRAINEE.equals(operation)) {
            handleGetTrainingsByTrainee(payload, rawMessage);
        } else if (OP_GET_TRAININGS_BY_TRAINER.equals(operation)) {
            handleGetTrainingsByTrainer(payload, rawMessage);
        } else if (OP_CREATE_TRAINING.equals(operation)) {
            handleCreateTraining(payload, rawMessage);
        } else {
            sendReply(rawMessage.getJMSReplyTo(), null, rawMessage);
        }
    }

    private void handleGetTrainingsByTrainee(Object payload, Message rawMessage) throws JMSException {
        if (!(payload instanceof GetTraineeTrainingsServiceRequest)) {
            sendReply(rawMessage.getJMSReplyTo(), null, rawMessage);
            return;
        }
        GetTraineeTrainingsServiceRequest req = (GetTraineeTrainingsServiceRequest) payload;
        String traineeId = req.getTraineeId();

        List<TrainingEntity> trainings = trainingRepository.findByTraineeId(traineeId);
        List<TrainingDto> dtos = trainings.stream()
                .map(this::toDto)
                .collect(Collectors.toList());

        GetTraineeTrainingsServiceResponse resp = new GetTraineeTrainingsServiceResponse(dtos);
        sendReply(rawMessage.getJMSReplyTo(), resp, rawMessage);
    }

    private void handleGetTrainingsByTrainer(Object payload, Message rawMessage) throws JMSException {
        if (!(payload instanceof GetTrainerTrainingsServiceRequest)) {
            sendReply(rawMessage.getJMSReplyTo(), null, rawMessage);
            return;
        }
        GetTrainerTrainingsServiceRequest req = (GetTrainerTrainingsServiceRequest) payload;
        String trainerId = req.getTrainerId();

        List<TrainingEntity> trainings = trainingRepository.findByTrainerId(trainerId);
        List<TrainingDto> dtos = trainings.stream()
                .map(this::toDto)
                .collect(Collectors.toList());

        GetTrainerTrainingsServiceResponse resp = new GetTrainerTrainingsServiceResponse(dtos);
        sendReply(rawMessage.getJMSReplyTo(), resp, rawMessage);
    }

    @Transactional
    private void handleCreateTraining(Object payload, Message rawMessage) throws JMSException {
        if (!(payload instanceof CreateTrainingRequest)) {
            sendReply(rawMessage.getJMSReplyTo(), null, rawMessage);
            return;
        }
        CreateTrainingRequest req = (CreateTrainingRequest) payload;

        TrainingEntity entity = new TrainingEntity();
        entity.setTraineeId(req.traineeId());
        entity.setTrainerId(req.trainerId());
        entity.setTrainingName(req.trainingName());
        entity.setTrainingTypeId(req.trainingTypeId());
        entity.setDate(req.date());
        entity.setDuration(req.duration());

        TrainingEntity saved = trainingRepository.save(entity);

        CreateTrainingResponse resp = new CreateTrainingResponse(saved.getId());
        sendReply(rawMessage.getJMSReplyTo(), resp, rawMessage);
    }

    private void sendReply(Destination replyTo, Object responsePayload, Message requestMessage) {
        if (replyTo == null) return;
        jmsTemplate.convertAndSend(replyTo, responsePayload, m -> {
            try {
                m.setJMSCorrelationID(requestMessage.getJMSCorrelationID());
            } catch (JMSException ignored) { }
            return m;
        });
    }

    private TrainingDto toDto(TrainingEntity t) {
        return new TrainingDto(
                t.getId(),
                t.getTraineeId(),
                t.getTrainerId(),
                t.getTrainingName(),
                t.getTrainingTypeId(),
                t.getDate(),
                t.getDuration()
        );
    }
}
