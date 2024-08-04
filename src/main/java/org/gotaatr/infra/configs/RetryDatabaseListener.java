package org.gotaatr.infra.configs;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.gotaatr.database.ConnectionHolder;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.RetryListener;
import org.springframework.stereotype.Component;

@Component
@Log4j2
@AllArgsConstructor
public class RetryDatabaseListener implements RetryListener {
    private final ConnectionHolder connectionHolder;
    @Override
    public <T, E extends Throwable> void onError(RetryContext context, RetryCallback<T, E> callback, Throwable throwable) {
        connectionHolder.retryConnect();
        log.info("Error occurred while connecting to the database. Retrying..., {}", throwable.getMessage());
        RetryListener.super.onError(context, callback, throwable);
    }
}
