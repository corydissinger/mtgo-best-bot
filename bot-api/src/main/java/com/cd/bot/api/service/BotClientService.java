package com.cd.bot.api.service;

import com.cd.bot.client.model.LifecycleEvent;
import com.cd.bot.client.model.LifecycleEventOutcome;
import com.cd.bot.model.domain.repository.BotCameraRepository;
import com.cd.bot.wrapper.http.BotClientRestClient;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Cory on 5/24/2017.
 */
@Service
public class BotClientService {

    @Autowired
    private BotClientRestClient botClientRestClient;

    @Autowired
    private BotCameraRepository botCameraRepository;

    public LifecycleEventOutcome runClient(LifecycleEvent lifecycleEvent) {
        LifecycleEventOutcome outcome = botClientRestClient.sendLifecycleEvent(lifecycleEvent);

        if(outcome.getBotCamera() != null && ArrayUtils.isNotEmpty(outcome.getBotCamera().getScreenShot())) {
            botCameraRepository.save(outcome.getBotCamera());
        }

        return outcome;
    }
}
