package com.cd.bot.client.wrapper.util;

import com.cd.bot.model.domain.PlayerBot;
import com.cd.bot.model.domain.bot.AssumedScreenTest;
import com.cd.bot.model.domain.bot.LifecycleEvent;
import com.cd.bot.model.domain.bot.ProcessingLifecycleStatus;

import java.util.Date;

/**
 * Created by Cory on 5/25/2017.
 */
public class LifecycleEventFactory {
    public static LifecycleEvent getLogin(final PlayerBot playerBot) {
        return new LifecycleEvent(AssumedScreenTest.LOGIN, ProcessingLifecycleStatus.UNKNOWN, playerBot, new Date());
    }

    public static LifecycleEvent get(final PlayerBot playerBot) {
        return new LifecycleEvent(AssumedScreenTest.TRADE, ProcessingLifecycleStatus.UNKNOWN, playerBot, new Date());
    }
}
