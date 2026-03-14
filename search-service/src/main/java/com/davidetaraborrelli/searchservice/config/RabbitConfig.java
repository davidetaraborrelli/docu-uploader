package com.davidetaraborrelli.searchservice.config;

import com.davidetaraborrelli.searchservice.messaging.DocumentEvent;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.DefaultClassMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class RabbitConfig {

    public static final String EXCHANGE_NAME = "docuupload.events";
    public static final String INDEXING_QUEUE = "indexing.queue";

    @Bean
    public TopicExchange docuUploadExchange() {
        return new TopicExchange(EXCHANGE_NAME);
    }

    @Bean
    public Queue indexingQueue() {
        return new Queue(INDEXING_QUEUE, true);
    }

    @Bean
    public Binding binding(Queue indexingQueue, TopicExchange docuUploadExchange) {
        return BindingBuilder.bind(indexingQueue)
                .to(docuUploadExchange)
                .with("document.uploaded");
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter();
        DefaultClassMapper classMapper = new DefaultClassMapper();
        classMapper.setTrustedPackages("*");
        classMapper.setIdClassMapping(Map.of(
                "com.davidetaraborrelli.documentservice.messaging.DocumentEvent",
                DocumentEvent.class
        ));
        converter.setClassMapper(classMapper);
        return converter;
    }
}
