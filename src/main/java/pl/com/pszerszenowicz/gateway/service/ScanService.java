package pl.com.pszerszenowicz.gateway.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Header;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyFuture;
import org.springframework.stereotype.Service;
import org.springframework.util.SerializationUtils;
import pl.com.pszerszenowicz.gateway.configuration.KafkaConfig;
import pl.com.pszerszenowicz.gateway.configuration.RabbitMQConfig;
import pl.com.pszerszenowicz.model.ProductInfo;
import pl.com.pszerszenowicz.model.VerificationStatus;
import pl.com.pszerszenowicz.gateway.model.dto.ReceivedScan;

import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Slf4j
@Service
@AllArgsConstructor
public class ScanService {

    RabbitTemplate rabbitTemplate;
    ReplyingKafkaTemplate<String, String, ProductInfo> replyingKafkaTemplate;

    public ResponseEntity<?> sendScan(ReceivedScan receivedScan) {
        log.info("Received scan: {}", receivedScan);
        VerificationStatus verificationStatus = verifyProductInVerificationMS(receivedScan.getBarcode());
        ProductInfo productInfo = getProductInfoInProductMS(receivedScan.getBarcode());

        return ResponseEntity.ok().body(productInfo);
    }

    private VerificationStatus verifyProductInVerificationMS(String barcode) {
        byte[] dataToSend = barcode.getBytes();
        Message toSend = MessageBuilder.withBody(dataToSend).build();
        Message result = rabbitTemplate.sendAndReceive(RabbitMQConfig.EXCHANGE_NAME, RabbitMQConfig.SENDING_QUEUE, toSend);

        VerificationStatus response = VerificationStatus.verificationError;
        if (result != null) {
            String correlationId = toSend.getMessageProperties().getCorrelationId();
            Map<String, Object> headers = result.getMessageProperties().getHeaders();
            String msgId = (String) headers.get("spring_returned_message_correlation");
            if (msgId.equals(correlationId)) {
                response = (VerificationStatus) SerializationUtils.deserialize(result.getBody());
            }
        }
        return response;
    }

    private ProductInfo getProductInfoInProductMS(String barcode) {
        replyingKafkaTemplate.setReplyErrorChecker(record -> {
            Header error = record.headers().lastHeader("serverSentAnError");
            if (error != null) {
                log.info("Server sent an error: {}", new String(error.value()));
            }
            return null;
        });

        ProducerRecord<String, String> record = new ProducerRecord<>(KafkaConfig.kafkaRequest, barcode);
        RequestReplyFuture<String, String, ProductInfo> future = replyingKafkaTemplate.sendAndReceive(record);
        ProductInfo returnProduct = null;
        try {
            future.getSendFuture().get(10, TimeUnit.SECONDS);
            ConsumerRecord<String, ProductInfo> consumerRecord = future.get(10, TimeUnit.SECONDS);
            returnProduct = consumerRecord.value();
            log.info("Received return product: {}", returnProduct);
        } catch (InterruptedException e) {
            log.info("InterruptedException: {}", e.getMessage());
        } catch (ExecutionException e) {
            log.info("ExecutionException: {}", e.getMessage());
        } catch (TimeoutException e) {
            log.info("TimeoutException: {}", e.getMessage());
        }
        return returnProduct;
    }
}