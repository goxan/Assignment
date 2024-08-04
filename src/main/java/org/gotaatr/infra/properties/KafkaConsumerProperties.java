package org.gotaatr.infra.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "kafka.consumer")
@Data
public class KafkaConsumerProperties {
    private String bootstrapServers;
    private String groupId;
    private String autoOffsetReset;
    private int fetchMinBytes;
    private int fetchMaxWaitMs;
    private int maxPollIntervalMs;
    private int maxPollRecords;

}
