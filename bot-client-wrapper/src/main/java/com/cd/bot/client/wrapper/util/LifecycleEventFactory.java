package com.cd.bot.client.wrapper.util;

import com.cd.bot.client.model.AssumedScreenTest;
import com.cd.bot.client.model.LifecycleEvent;
import com.cd.bot.client.model.ProcessingLifecycleStatus;

/**
 * Created by Cory on 5/25/2017.
 */
public class LifecycleEventFactory {
    public static LifecycleEvent getLogin() {
        return new LifecycleEvent(AssumedScreenTest.LOGIN, ProcessingLifecycleStatus.UNKNOWN);
    }

    public static LifecycleEvent get() {
        return new LifecycleEvent(AssumedScreenTest.TRADE, ProcessingLifecycleStatus.UNKNOWN);
    }
}
