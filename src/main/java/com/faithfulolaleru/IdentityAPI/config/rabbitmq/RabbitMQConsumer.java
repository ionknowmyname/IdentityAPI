package com.faithfulolaleru.IdentityAPI.config.rabbitmq;

import com.faithfulolaleru.IdentityAPI.dto.LoginResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
// @EnableRabbit
public class RabbitMQConsumer {

    @RabbitListener(queues = {"${rabbitmq.queue.name}"})
    public void consume(String message) {
        log.info(String.format("Received message --> %s", message));
    }

    @RabbitListener(queues = {"${rabbitmq.queue.json.name}"})
    public void consume2(String message) { // try LoginResponse response
        log.info(String.format("Received Json response --> %s", message));
    }
}
