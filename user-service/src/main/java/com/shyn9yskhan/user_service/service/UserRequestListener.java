package com.shyn9yskhan.user_service.service;

import com.shyn9yskhan.user_service.dto.GetUserIdByUsernameServiceRequest;
import com.shyn9yskhan.user_service.dto.GetUserIdByUsernameServiceResponse;
import com.shyn9yskhan.user_service.entity.UserEntity;
import com.shyn9yskhan.user_service.repository.UserRepository;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import jakarta.jms.Destination;
import jakarta.jms.JMSException;
import jakarta.jms.Message;
import java.util.Optional;

@Component
public class UserRequestListener {

    private final UserRepository userRepository;
    private final JmsTemplate jmsTemplate;

    private static final String OPERATION_HEADER = "operation";
    private static final String OP_GET_USER_ID_BY_USERNAME = "GetUserIdByUsername";

    public UserRequestListener(UserRepository userRepository, JmsTemplate jmsTemplate) {
        this.userRepository = userRepository;
        this.jmsTemplate = jmsTemplate;
    }

    @JmsListener(destination = "user.request.queue", containerFactory = "jmsListenerContainerFactory")
    public void onMessage(Object payload, Message rawMessage) throws JMSException {
        String operation = null;
        try { operation = rawMessage.getStringProperty(OPERATION_HEADER); } catch (JMSException ignored) {}
        if (OP_GET_USER_ID_BY_USERNAME.equals(operation)) {
            handleGetUserIdByUsername(payload, rawMessage);
        }
    }

    private void handleGetUserIdByUsername(Object payload, Message rawMessage) throws JMSException {
        if (!(payload instanceof GetUserIdByUsernameServiceRequest)) {
            sendReply(rawMessage.getJMSReplyTo(), null, rawMessage);
            return;
        }
        GetUserIdByUsernameServiceRequest req = (GetUserIdByUsernameServiceRequest) payload;
        String username = req.getUsername();
        Optional<UserEntity> maybe = userRepository.findByUsername(username);
        String userId = maybe.map(UserEntity::getId).orElse(null);
        GetUserIdByUsernameServiceResponse resp = new GetUserIdByUsernameServiceResponse(userId);
        sendReply(rawMessage.getJMSReplyTo(), resp, rawMessage);
    }

    private void sendReply(Destination replyTo, Object responsePayload, Message requestMessage) {
        if (replyTo == null) return;
        jmsTemplate.convertAndSend(replyTo, responsePayload, m -> {
            try { m.setJMSCorrelationID(requestMessage.getJMSCorrelationID()); } catch (JMSException ignored) {}
            return m;
        });
    }
}
