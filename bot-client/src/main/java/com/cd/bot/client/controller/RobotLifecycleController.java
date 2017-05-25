package com.cd.bot.client.controller;

import com.cd.bot.client.config.BotConfig;
import com.cd.bot.client.model.LifecycleEvent;
import com.cd.bot.client.model.LifecycleEventOutcome;
import com.cd.bot.client.robot.RobotMaster;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by Cory on 5/23/2017.
 */
@RestController
public class RobotLifecycleController {

    @Autowired
    private RobotMaster robotMaster;

    private AtomicBoolean isRunning = new AtomicBoolean(false);

    @PreAuthorize(BotConfig.HAS_AUTH_ROLE_MASTER)
    @RequestMapping(value = "/status", method = RequestMethod.GET)
    public ResponseEntity<Void> status() {
        if(isRunning.get()) {
            return new ResponseEntity<>(HttpStatus.PROCESSING);
        } else {
            return new ResponseEntity<>(HttpStatus.OK);
        }

    }

    @PreAuthorize(BotConfig.HAS_AUTH_ROLE_MASTER)
    @RequestMapping(value = "/client-bot", method = RequestMethod.POST)
    public @ResponseBody LifecycleEventOutcome runBot(@RequestBody final LifecycleEvent lifecycleEvent) {
        isRunning.set(true);
        LifecycleEventOutcome outcome = robotMaster.runBot(lifecycleEvent);
        isRunning.set(false);
        return outcome;
    }
}
