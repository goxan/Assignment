package org.gotaatr.infra.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "connection")
@Data
public class ConnectionProperties {
    private String url;
    private String username;
    private String password;

}
