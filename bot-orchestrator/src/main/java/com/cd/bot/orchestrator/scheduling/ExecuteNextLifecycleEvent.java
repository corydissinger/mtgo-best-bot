package com.cd.bot.orchestrator.scheduling;

import com.cd.bot.model.domain.bot.LifecycleEvent;
import com.cd.bot.orchestrator.kafka.LifecycleEventSender;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by Cory on 6/17/2017.
 */
public class ExecuteNextLifecycleEvent implements Runnable {

    private LifecycleEvent lifecycleEvent;
    private LifecycleEventSender lifecycleEventSender;
    private String botTopic;

    @Autowired
    public ExecuteNextLifecycleEvent(LifecycleEvent lifecycleEvent, LifecycleEventSender lifecycleEventSender, String botTopic) {
        this.lifecycleEvent = lifecycleEvent;
        this.lifecycleEventSender = lifecycleEventSender;
        this.botTopic = botTopic;
    }

    @Override
    public void run() {
        lifecycleEventSender.send(botTopic, lifecycleEvent);
    }
}
