package org.gotaatr.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.gotaatr.database.TimeRecordEntity;
import org.gotaatr.database.TimeRecordRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Log4j2
@AllArgsConstructor
@Profile("!test")
public class KafkaConsumer {

    private final ObjectMapper objectMapper;
    private final TimeRecordRepository timeRecordRepository;
        @SneakyThrows
        @KafkaListener(topics = "timetable", clientIdPrefix = "listenerForDatabase", batch = "true")
        public void listen(List<String> data) {
            List<TimeRecordEntity> timeBucket = data
                    .stream()
                    .map(this::silentMapper)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .toList();
            timeRecordRepository.saveAll(timeBucket);
        }

        private Optional<TimeRecordEntity> silentMapper(String data) {
            try {
                return Optional.of(objectMapper.readValue(data, TimeRecordEntity.class));
            } catch (Exception e) {
                log.warn("Some error occurred while mapping data: {}", e.getMessage());
                return Optional.empty();
            }
        }
    }
