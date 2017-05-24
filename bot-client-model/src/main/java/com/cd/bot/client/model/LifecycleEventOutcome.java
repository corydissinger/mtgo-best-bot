package com.cd.bot.client.model;

import com.cd.bot.model.domain.BotCamera;

import java.io.Serializable;

/**
 * Created by Cory on 5/23/2017.
 */
public class LifecycleEventOutcome implements Serializable {
    private BotCamera botCamera;

    public BotCamera getBotCamera() {
        return botCamera;
    }

    public void setBotCamera(BotCamera botCamera) {
        this.botCamera = botCamera;
    }
}
