package org.gotaatr.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.gotaatr.services.TimeRecordDTO;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Log4j2
public class KafkaProducer {

    private static final String TOPIC = "timetable";

    private final ObjectMapper objectMapper;
    private KafkaTemplate<String, String> kafkaTemplate;

    public void sendMessage(TimeRecordDTO message) {
        try {
            kafkaTemplate
                    .send(TOPIC, objectMapper.writeValueAsString(message))
                    .thenRun(() -> log.info("Message sent: " + message));
        } catch (JsonProcessingException e) {
            log.error("Error while sending message: {}", e.getMessage());
        }
    }
}
