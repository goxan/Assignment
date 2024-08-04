package org.gotaatr;

import lombok.AllArgsConstructor;
import org.gotaatr.kafka.KafkaProducer;
import org.gotaatr.services.TimeRecordDTO;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
@Profile("!test")
public class MessageGenerationScheduler {
    private final KafkaProducer kafkaProducer;

    @Scheduled(fixedRate = 1000)
    public void schedule() {
        var timeRecord = new TimeRecordDTO(LocalDateTime.now().toString());
        kafkaProducer.sendMessage(timeRecord);
    }
}
