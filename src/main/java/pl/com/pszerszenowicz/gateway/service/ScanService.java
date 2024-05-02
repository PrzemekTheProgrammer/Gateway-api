package pl.com.pszerszenowicz.gateway.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.SerializationUtils;
import pl.com.pszerszenowicz.gateway.configuration.RabbitMQConfig;
import pl.com.pszerszenowicz.model.VerificationStatus;
import pl.com.pszerszenowicz.gateway.model.dto.ReceivedScan;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@AllArgsConstructor
public class ScanService {

    RabbitTemplate rabbitTemplate;

    public ResponseEntity<?> sendScan(ReceivedScan receivedScan) {
        log.info("Received scan: {}", receivedScan);
        VerificationStatus verificationStatus = verifyProductInVerificationMS(receivedScan.getBarcode());

        return ResponseEntity.ok().body(verificationStatus);
    }

    private VerificationStatus verifyProductInVerificationMS(String barcode) {
        byte[] dataToSend = barcode.getBytes();
        Message toSend = MessageBuilder.withBody(dataToSend).build();
        Message result = rabbitTemplate.sendAndReceive(RabbitMQConfig.EXCHANGE_NAME, RabbitMQConfig.SENDING_QUEUE, toSend);

        VerificationStatus response = VerificationStatus.verificationError;
        if( result != null) {
            String correlationId = toSend.getMessageProperties().getCorrelationId();
            Map<String,Object> headers = result.getMessageProperties().getHeaders();
            String msgId = (String) headers.get("spring_returned_message_correlation");
            if (msgId.equals(correlationId)) {
                response = (VerificationStatus) SerializationUtils.deserialize(result.getBody());
            }
        }
        return response;
    }
}
