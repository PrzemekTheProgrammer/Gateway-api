package pl.com.pszerszenowicz.gateway.configuration;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import pl.com.pszerszenowicz.model.ProductInfo;
import pl.com.pszerszenowicz.model.ProductInfoDeserializer;

import java.util.HashMap;
import java.util.Map;

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

//    @Bean
//    public ProducerFactory<String, String> producerFactory() {
//        Map<String,Object> configProps = new HashMap<>();
//        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
//        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
//        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
//        return new DefaultKafkaProducerFactory<>(configProps);
//    }

//    @Bean
//    public ConsumerFactory<String, ProductInfo> consumerFactory() {
//        Map<String, Object> configProps = new HashMap<>();
//        configProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
//        configProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
//        configProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ProductInfoDeserializer.class);
//        configProps.put(ConsumerConfig.GROUP_ID_CONFIG, "repliesGroup");
//        return new DefaultKafkaConsumerFactory<>(configProps);
//    }

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
