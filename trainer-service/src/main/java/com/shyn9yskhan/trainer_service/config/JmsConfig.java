package com.shyn9yskhan.trainer_service.config;

import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageType;

import jakarta.jms.ConnectionFactory;

@Configuration
public class JmsConfig {

    @Bean
    public MappingJackson2MessageConverter jacksonJmsMessageConverter() {
        MappingJackson2MessageConverter conv = new MappingJackson2MessageConverter();
        conv.setTargetType(MessageType.TEXT);
        conv.setTypeIdPropertyName("_type");
        return conv;
    }

    @Bean
    public JmsTemplate jmsTemplate(ConnectionFactory cf, MappingJackson2MessageConverter converter) {
        JmsTemplate template = new JmsTemplate(cf);
        template.setMessageConverter(converter);
        template.setReceiveTimeout(5000L);
        return template;
    }

    @Bean
    public DefaultJmsListenerContainerFactory jmsListenerContainerFactory(
            ConnectionFactory connectionFactory,
            DefaultJmsListenerContainerFactoryConfigurer configurer) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        configurer.configure(factory, connectionFactory);
        factory.setConcurrency("3-10");
        factory.setSessionTransacted(true);
        return factory;
    }
}
