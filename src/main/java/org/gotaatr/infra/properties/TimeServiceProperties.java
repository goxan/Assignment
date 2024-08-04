package org.gotaatr.infra.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "time-service")
@Data
public class TimeServiceProperties {
    private int defaultPageSize;
    private int defaultPageNumber;
}
