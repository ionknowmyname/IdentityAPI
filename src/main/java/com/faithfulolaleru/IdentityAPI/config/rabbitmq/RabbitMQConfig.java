package com.faithfulolaleru.IdentityAPI.config.rabbitmq;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Value("${spring.rabbitmq.host}")
    private String host;

    @Value("${spring.rabbitmq.port}")
    private int port;

    @Value("${spring.rabbitmq.username}")
    private String username;

    @Value("${spring.rabbitmq.password}")
    private String password;

    @Value("${rabbitmq.queue.name}")
    private String queueName;

    @Value("${rabbitmq.exchange.name}")
    private String exchangeName;

    @Value("${rabbitmq.routing.key}")
    private String routingKey;

//    @Value("${spring.rabbitmq.virtual-host}")
//    private String virtualHost;



    @Bean
    public Queue queue() {
        return new Queue(queueName);
    }

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(exchangeName);
    }

//    @Bean
//    public DirectExchange exchange2() {
//        return new DirectExchange(exchangeName);
//    }

    // bind queue to the exchange using routing key
    @Bean
    public Binding binding() {
        return BindingBuilder
                .bind(queue())
                .to(exchange())
                .with(routingKey);
    }

    /*
        Spring Boot autoconfigures below beans:

        // ConnectionFactory  // this is no longer autoconfigured
        // RabbitTemplate  // this no longer is autoconfigured
        // RabbitAdmin   // this no longer is autoconfigured

    */


    @Bean
    public CachingConnectionFactory connectionFactory() {
        // return new CachingConnectionFactory("localhost");
        return new CachingConnectionFactory(host);
    }

    @Bean
    public CachingConnectionFactory connectionFactory2() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setHost(host);
        connectionFactory.setPort(port);
        connectionFactory.setUsername(username);
        connectionFactory.setPassword(password);
        // connectionFactory.setVirtualHost(virtualHost);

        return connectionFactory;
    }

    @Bean
    public RabbitAdmin amqpAdmin() {
        return new RabbitAdmin(connectionFactory2());
    }

    // works for string being sent to rabbitmq queue
    @Bean
    public Jackson2JsonMessageConverter converter() {
        return new Jackson2JsonMessageConverter();
    }

    // works for when you sending LoginResponse/any object to rabbitmq queue
    @Bean
    public MessageConverter converter2() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule()); // added to fix Instant time from LoginResponse to rabbitmq

        return new Jackson2JsonMessageConverter(objectMapper);
    }

    @Bean
    // @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public RabbitTemplate rabbitTemplate() {
        RabbitTemplate template = new RabbitTemplate(connectionFactory2());
        template.setMessageConverter(converter2());

        return template;
    }

    /*
        @Bean
        public RabbitTemplate rabbitTemplate(Jackson2JsonMessageConverter converter) {
            RabbitTemplate template =  new RabbitTemplate(connectionFactory());
            template.setMessageConverter(converter);

            return template;
        }
    */
}
