package com.cd.bot.orchestrator.kafka;

import com.cd.bot.model.domain.bot.LifecycleEventOutcome;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;

/**
 * Created by Cory on 6/8/2017.
 */
public class LifecycleOutcomeReceiver {
    private static final Logger LOGGER = LoggerFactory.getLogger(LifecycleOutcomeReceiver.class);

    @KafkaListener(topics = "${kafka.topic.outcome}")
    public void receiveOutcome(final LifecycleEventOutcome outcome) {
        System.out.println(outcome);
    }
}
