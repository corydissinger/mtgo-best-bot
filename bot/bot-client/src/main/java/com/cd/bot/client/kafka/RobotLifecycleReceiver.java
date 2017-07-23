package com.cd.bot.client.kafka;

import com.cd.bot.model.domain.bot.LifecycleEvent;
import com.cd.bot.client.robot.RobotMaster;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;

import java.util.concurrent.BlockingQueue;

/**
 * Created by Cory on 5/23/2017.
 */
public class RobotLifecycleReceiver {
    private static final Logger LOGGER = LoggerFactory.getLogger(RobotLifecycleReceiver.class);

    @Autowired
    private BlockingQueue<LifecycleEvent> processingEventsQueue;

    @Autowired
    private RobotMaster robotMaster;

    @KafkaListener(topics = "${kafka.topic.bot}")
    public void runBot(final LifecycleEvent lifecycleEvent) {
        if(!processingEventsQueue.contains(lifecycleEvent)) {
            processingEventsQueue.add(lifecycleEvent);
        }
    }
}
