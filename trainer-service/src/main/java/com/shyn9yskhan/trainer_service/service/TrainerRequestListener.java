package com.shyn9yskhan.trainer_service.service;

import com.shyn9yskhan.trainer_service.dto.GetTrainerIdByUserIdServiceRequest;
import com.shyn9yskhan.trainer_service.dto.GetTrainerIdByUserIdServiceResponse;
import com.shyn9yskhan.trainer_service.dto.GetTrainingTypeIdByTrainerIdServiceRequest;
import com.shyn9yskhan.trainer_service.dto.GetTrainingTypeIdByTrainerIdServiceResponse;
import com.shyn9yskhan.trainer_service.entity.TrainerEntity;
import com.shyn9yskhan.trainer_service.repository.TrainerRepository;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import jakarta.jms.Destination;
import jakarta.jms.JMSException;
import jakarta.jms.Message;
import java.util.Optional;

@Component
public class TrainerRequestListener {

    private final TrainerRepository trainerRepository;
    private final JmsTemplate jmsTemplate;

    private static final String OPERATION_HEADER = "operation";
    private static final String OP_GET_TRAINER_BY_USER_ID = "GetTrainerIdByUserId";
    private static final String OP_GET_TRAINING_TYPE_BY_TRAINER_ID = "GetTrainingTypeIdByTrainerId";

    public TrainerRequestListener(TrainerRepository trainerRepository, JmsTemplate jmsTemplate) {
        this.trainerRepository = trainerRepository;
        this.jmsTemplate = jmsTemplate;
    }

    @JmsListener(destination = "trainer.request.queue", containerFactory = "jmsListenerContainerFactory")
    public void onMessage(Object payload, Message rawMessage) throws JMSException {
        String operation = null;
        try {
            operation = rawMessage.getStringProperty(OPERATION_HEADER);
        } catch (JMSException ignored) {}

        if (OP_GET_TRAINER_BY_USER_ID.equals(operation)) {
            handleGetTrainerIdByUserId(payload, rawMessage);
        } else if (OP_GET_TRAINING_TYPE_BY_TRAINER_ID.equals(operation)) {
            handleGetTrainingTypeIdByTrainerId(payload, rawMessage);
        } else {}
    }

    private void handleGetTrainerIdByUserId(Object payload, Message rawMessage) throws JMSException {
        if (!(payload instanceof GetTrainerIdByUserIdServiceRequest)) {
            sendReply(rawMessage.getJMSReplyTo(), null, rawMessage);
            return;
        }
        GetTrainerIdByUserIdServiceRequest request = (GetTrainerIdByUserIdServiceRequest) payload;
        String userId = request.getUserId();
        String trainerId = null;

        Optional<TrainerEntity> maybe = trainerRepository.findByUserId(userId);
        if (maybe.isPresent()) {
            trainerId = maybe.get().getId();
        }
        GetTrainerIdByUserIdServiceResponse resp = new GetTrainerIdByUserIdServiceResponse(trainerId);
        sendReply(rawMessage.getJMSReplyTo(), resp, rawMessage);
    }

    private void handleGetTrainingTypeIdByTrainerId(Object payload, Message rawMessage) throws JMSException {
        if (!(payload instanceof GetTrainingTypeIdByTrainerIdServiceRequest)) {
            sendReply(rawMessage.getJMSReplyTo(), null, rawMessage);
            return;
        }
        GetTrainingTypeIdByTrainerIdServiceRequest request = (GetTrainingTypeIdByTrainerIdServiceRequest) payload;
        String trainerId = request.getTrainerId();
        String trainingTypeId = null;

        Optional<TrainerEntity> trainerOpt = trainerRepository.findById(trainerId);
        if (trainerOpt.isPresent()) {
            trainingTypeId = trainerOpt.get().getSpecialization();
        }
        GetTrainingTypeIdByTrainerIdServiceResponse resp = new GetTrainingTypeIdByTrainerIdServiceResponse(trainingTypeId);
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
}
