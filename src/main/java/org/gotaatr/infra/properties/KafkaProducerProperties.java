package org.gotaatr.infra.properties;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "kafka.producer")
@Data
public class KafkaProducerProperties {
    private String bootstrapServers;
    private int batchSize;
    private int bufferMemory;
    private int lingerMs;
}
