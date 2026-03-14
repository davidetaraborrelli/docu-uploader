package com.davidetaraborrelli.notificationservice.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    public static final String EXCHANGE_NAME = "docuupload.events";
    public static final String NOTIFICATION_QUEUE = "notification.queue";

    @Bean
    public TopicExchange docuUploadExchange() {
        return new TopicExchange(EXCHANGE_NAME);
    }

    @Bean
    public Queue notificationQueue() {
        return new Queue(NOTIFICATION_QUEUE, true);
    }

    @Bean
    public Binding binding(Queue notificationQueue, TopicExchange docuUploadExchange) {
        return BindingBuilder.bind(notificationQueue)
                .to(docuUploadExchange)
                .with("document.*");
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
