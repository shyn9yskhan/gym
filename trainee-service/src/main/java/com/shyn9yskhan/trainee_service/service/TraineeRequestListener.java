package com.shyn9yskhan.trainee_service.service;

import com.shyn9yskhan.trainee_service.dto.GetTraineeIdByUserIdRequest;
import com.shyn9yskhan.trainee_service.dto.GetTraineeIdByUserIdResponse;
import com.shyn9yskhan.trainee_service.entity.TraineeEntity;
import com.shyn9yskhan.trainee_service.repository.TraineeRepository;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import jakarta.jms.Destination;
import jakarta.jms.JMSException;
import jakarta.jms.Message;
import java.util.Optional;

@Component
public class TraineeRequestListener {

    private final TraineeRepository traineeRepository;
    private final JmsTemplate jmsTemplate;

    private static final String OPERATION_HEADER = "operation";
    private static final String OP_GET_TRAINEE_BY_USER_ID = "GetTraineeIdByUserId";

    public TraineeRequestListener(TraineeRepository traineeRepository, JmsTemplate jmsTemplate) {
        this.traineeRepository = traineeRepository;
        this.jmsTemplate = jmsTemplate;
    }

    @JmsListener(destination = "trainee.request.queue", containerFactory = "jmsListenerContainerFactory")
    public void onMessage(Object payload, Message rawMessage) throws JMSException {
        String operation = null;
        try {
            operation = rawMessage.getStringProperty(OPERATION_HEADER);
        } catch (JMSException ignored) {}

        if (OP_GET_TRAINEE_BY_USER_ID.equals(operation)) {
            handleGetTraineeIdByUserId(payload, rawMessage);
        } else {}
    }

    private void handleGetTraineeIdByUserId(Object payload, Message rawMessage) throws JMSException {
        if (!(payload instanceof GetTraineeIdByUserIdRequest)) {
            sendReply(rawMessage.getJMSReplyTo(), null, rawMessage);
            return;
        }

        GetTraineeIdByUserIdRequest request = (GetTraineeIdByUserIdRequest) payload;
        String userId = request.getUserId();
        String traineeId = null;

        Optional<TraineeEntity> maybe = traineeRepository.findByUserId(userId);
        if (maybe.isPresent()) {
            traineeId = maybe.get().getId();
        }

        GetTraineeIdByUserIdResponse response = new GetTraineeIdByUserIdResponse(traineeId);

        Destination replyTo = rawMessage.getJMSReplyTo();
        sendReply(replyTo, response, rawMessage);
    }

    private void sendReply(Destination replyTo, Object responsePayload, Message requestMessage) {
        if (replyTo == null) {
            return;
        }
        jmsTemplate.convertAndSend(replyTo, responsePayload, m -> {
            try {
                m.setJMSCorrelationID(requestMessage.getJMSCorrelationID());
            } catch (JMSException ignored) { }
            return m;
        });
    }
}
