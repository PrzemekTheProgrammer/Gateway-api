package pl.com.pszerszenowicz.gateway.configuration;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE_NAME = "fromApiGateway";
    public static final String SENDING_QUEUE = "sentFromApiGateway";
    public static final String RECEIVING_QUEUE = "receivedByApiGateway";

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory("localhost");
        connectionFactory.setPort(5672);
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");
        connectionFactory.setPublisherReturns(true);
        connectionFactory.setPublisherConfirmType(CachingConnectionFactory.ConfirmType.CORRELATED);
        return connectionFactory;
    }

    @Bean
    Queue sendingQueue() {
        return new Queue(SENDING_QUEUE);
    }

    @Bean
    Queue receivingQueue() {
        return new Queue(RECEIVING_QUEUE);
    }

    @Bean
    DirectExchange exchange() {
        return new DirectExchange(EXCHANGE_NAME);
    }

    @Bean
    Binding sendBinding() {
        return BindingBuilder.bind(sendingQueue()).to(exchange()).with(SENDING_QUEUE);
    }

    @Bean
    Binding receiveBinding() {
        return BindingBuilder.bind(receivingQueue()).to(exchange()).with(RECEIVING_QUEUE);
    }

    @Bean
    public RabbitTemplate rabbitTemplate() {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory());
        rabbitTemplate.setReplyAddress(RECEIVING_QUEUE);
        rabbitTemplate.setReplyTimeout(10000);
        return rabbitTemplate;
    }

    @Bean
    public SimpleMessageListenerContainer replyContainer() {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory());
        container.setQueueNames(RECEIVING_QUEUE);
        container.setMessageListener(rabbitTemplate());
        return container;
    }

}
