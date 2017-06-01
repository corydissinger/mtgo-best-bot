package com.cd.bot.orchestrator.service;

import com.cd.bot.client.model.LifecycleEvent;
import com.cd.bot.client.model.LifecycleEventOutcome;
import com.cd.bot.client.wrapper.ws.BotWebSocketClient;
import com.cd.bot.model.domain.repository.BotCameraRepository;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.function.Consumer;

/**
 * Created by Cory on 5/24/2017.
 */
@Service
public class BotClientService {

    @Autowired
    private BotWebSocketClient botWebSocketClient;

    @Autowired
    private BotCameraRepository botCameraRepository;

    public void pushEvent(final LifecycleEvent lifecycleEvent, final Consumer<LifecycleEventOutcome> parentOutcomeConsumer) {
        Consumer<LifecycleEventOutcome> outcomeConsumer = outcome -> {
            if(outcome.getBotCamera() != null && ArrayUtils.isNotEmpty(outcome.getBotCamera().getScreenShot())) {
                botCameraRepository.save(outcome.getBotCamera());
            }

            parentOutcomeConsumer.accept(outcome);
        };

        botWebSocketClient.sendLifecycleEvent(lifecycleEvent, outcomeConsumer);
    }
}