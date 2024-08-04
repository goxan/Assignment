package org.gotaatr.infra.configs;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.policy.AlwaysRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

@Configuration
@AllArgsConstructor
public class RetryTemplateConfiguration {
    private final RetryDatabaseListener retryDatabaseListener;

    @Bean
    public RetryTemplate retryTemplate() {
        return RetryTemplate.builder()
                .withListener(retryDatabaseListener)
                .fixedBackoff(5000)
                .customPolicy(new AlwaysRetryPolicy())
                .build();
    }

}