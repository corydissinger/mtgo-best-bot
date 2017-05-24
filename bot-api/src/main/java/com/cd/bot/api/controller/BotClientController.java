package com.cd.bot.api.controller;

import com.cd.bot.client.model.LifecycleEvent;
import com.cd.bot.client.model.LifecycleEventOutcome;
import com.cd.bot.wrapper.http.BotClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Cory on 5/23/2017.
 */
@RestController
public class BotClientController {
    @Autowired
    private BotClientService botClientService;

    @RequestMapping(value = "/client")
    public LifecycleEventOutcome runClient(LifecycleEvent lifecycleEvent) {
        return botClientService.sendLifecycleEvent(lifecycleEvent);
    }
}
