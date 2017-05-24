package com.cd.bot.client.controller;

import com.cd.bot.client.model.LifecycleEvent;
import com.cd.bot.client.model.LifecycleEventOutcome;
import com.cd.bot.client.robot.RobotMaster;
import com.cd.bot.model.domain.BotCamera;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Cory on 5/23/2017.
 */
@RestController
public class RobotLifecycleController {

    @Autowired
    private RobotMaster robotMaster;

    @RequestMapping(value = "/client-bot", method = RequestMethod.POST)
    public @ResponseBody LifecycleEventOutcome runBot(@RequestBody final LifecycleEvent lifecycleEvent) {
        BotCamera botCamera = robotMaster.runBot();

        LifecycleEventOutcome outcome = new LifecycleEventOutcome();

        outcome.setBotCamera(botCamera);

        return outcome; //TODO - add more things
    }
}
