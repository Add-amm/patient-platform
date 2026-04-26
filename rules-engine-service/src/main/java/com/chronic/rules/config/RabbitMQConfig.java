package com.chronic.rules.config;

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

    @Value("${rabbitmq.exchange.alert}")
    private String alertExchange;

    @Value("${rabbitmq.queue.alert}")
    private String alertQueue;

    @Value("${rabbitmq.routing-key.alert}")
    private String alertRoutingKey;

    // ── Queue VITALS (consommée par ce service) ───────────────────────────────

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

    // ── Queue ALERT (publiée par ce service vers alert-service) ───────────────

    @Bean
    public DirectExchange alertExchange() {
        return new DirectExchange(alertExchange);
    }

    @Bean
    public Queue alertQueue() {
        return QueueBuilder.durable(alertQueue).build();
    }

    @Bean
    public Binding alertBinding(Queue alertQueue, DirectExchange alertExchange) {
        return BindingBuilder.bind(alertQueue).to(alertExchange).with(alertRoutingKey);
    }

    // ── Convertisseur JSON ────────────────────────────────────────────────────

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