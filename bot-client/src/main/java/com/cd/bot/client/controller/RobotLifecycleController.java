package com.cd.bot.client.controller;

import com.cd.bot.client.model.LifecycleEvent;
import com.cd.bot.client.model.LifecycleEventOutcome;
import com.cd.bot.client.robot.RobotMaster;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

/**
 * Created by Cory on 5/23/2017.
 */
@Controller
public class RobotLifecycleController {

    @Autowired
    private RobotMaster robotMaster;

    @MessageMapping("/client-bot")
    @SendToUser("/client-bot")
    public LifecycleEventOutcome runBot(final LifecycleEvent lifecycleEvent) {
        return robotMaster.runBot(lifecycleEvent);
    }
}
