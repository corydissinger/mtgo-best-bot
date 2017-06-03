package com.cd.bot.orchestrator.kafka;

import com.cd.bot.model.domain.bot.LifecycleEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

/**
 * Created by Cory on 6/2/2017.
 */
public class LifecycleEventSender {

    private static final Logger LOGGER = LoggerFactory.getLogger(LifecycleEventSender.class);

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    public void send(String topic, LifecycleEvent message) {
        // the KafkaTemplate provides asynchronous send methods returning a Future
        ListenableFuture<SendResult<String, Object>> future = kafkaTemplate.send(topic, message);

        // register a callback with the listener to receive the result of the send asynchronously
        future.addCallback(new ListenableFutureCallback<SendResult<String, Object>>() {

            @Override
            public void onSuccess(SendResult<String, Object> result) {
                LOGGER.info("sent message='{}' with offset={}", message,
                        result.getRecordMetadata().offset());
            }

            @Override
            public void onFailure(Throwable ex) {
                LOGGER.error("unable to send message='{}'", message, ex);
            }
        });

        // or, to block the sending thread to await the result, invoke the future's get() method
    }
}
