package com.cd.bot.api.controller;

import com.cd.bot.api.service.BotClientService;
import com.cd.bot.client.model.LifecycleEvent;
import com.cd.bot.client.model.LifecycleEventOutcome;
import com.cd.bot.wrapper.http.BotClientRestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Cory on 5/23/2017.
 */
@RestController
public class BotClientController {

    @Autowired
    private BotClientService botClientService;

    @RequestMapping(value = "/client", method = RequestMethod.POST)
    public @ResponseBody LifecycleEventOutcome runClient(@RequestBody final LifecycleEvent lifecycleEvent) {
        LifecycleEventOutcome outcome = botClientService.runClient(lifecycleEvent);
        return outcome;
    }
}
