package com.faithfulolaleru.IdentityAPI.config.rabbitmq;

import com.faithfulolaleru.IdentityAPI.dto.LoginResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RabbitMQProducer {

    @Value("${rabbitmq.exchange.name}")
    private String exchangeName;

    @Value("${rabbitmq.routing.key}")
    private String routingKey;

    @Value("${rabbitmq.json.routing.key}")
    private String jsonRoutingKey;

    private final RabbitTemplate rabbitTemplate;

    public RabbitMQProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }



    public void sendMessage(String message) {
        log.info(String.format("Message sent --> %s", message));

        rabbitTemplate.convertAndSend(exchangeName, routingKey, message);
    }

    public void sendMessage2(LoginResponse response) {
        log.info(String.format("Message sent --> %s", response.toString()));

        rabbitTemplate.convertAndSend(exchangeName, jsonRoutingKey, response);
    }


}
