package pl.com.pszerszenowicz.gateway.configuration;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import pl.com.pszerszenowicz.model.ProductInfo;

@EnableKafka
@Configuration
public class KafkaConfig {
    public static final String kafkaRequest = "kafka-request-topic";

    public static final String kafkaReply = "kafka-reply-topic";

    public static final String bootstrapServers = "localhost:9092";

    @Bean
    public NewTopic kRequests() {
        return TopicBuilder.name(kafkaRequest)
                .partitions(3)
                .build();
    }

    @Bean
    public NewTopic kReplies() {
        return TopicBuilder.name(kafkaReply)
                .partitions(3)
                .build();
    }

    @Bean
    ConcurrentMessageListenerContainer<String, ProductInfo> repliesContainer(
            ConcurrentKafkaListenerContainerFactory<String, ProductInfo> containerFactory) {

        ConcurrentMessageListenerContainer<String, ProductInfo> repliesContainer =
                containerFactory.createContainer(kafkaReply);
        repliesContainer.getContainerProperties().setGroupId("repliesGroup");
        repliesContainer.setAutoStartup(false);
        return repliesContainer;
    }

    @Bean
    ReplyingKafkaTemplate<String, String, ProductInfo> replyingTemplate(
            ProducerFactory<String, String> pf,
            ConcurrentMessageListenerContainer<String, ProductInfo> repliesContainer) {
        return new ReplyingKafkaTemplate<>(pf, repliesContainer);
    }
}
