package com.chronic.vitals.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Value("${rabbitmq.exchange.vitals}")
    private String vitalsExchange;

    @Value("${rabbitmq.queue.vitals}")
    private String vitalsQueue;

    @Value("${rabbitmq.routing-key.vitals}")
    private String vitalsRoutingKey;

    @Bean
    public DirectExchange vitalsExchange() {
        return new DirectExchange(vitalsExchange);
    }

    @Bean
    public Queue vitalsQueue() {
        return QueueBuilder.durable(vitalsQueue).build();
    }

    @Bean
    public Binding vitalsBinding(Queue vitalsQueue, DirectExchange vitalsExchange) {
        return BindingBuilder.bind(vitalsQueue).to(vitalsExchange).with(vitalsRoutingKey);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jsonMessageConverter());
        return template;
    }
}