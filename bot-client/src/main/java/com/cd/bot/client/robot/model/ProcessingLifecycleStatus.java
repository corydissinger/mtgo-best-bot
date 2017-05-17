package com.cd.bot.client.robot.model;

/**
 * Created by Cory on 5/13/2017.
 */
public enum ProcessingLifecycleStatus {
    UNKNOWN,
    AWAIT_APPLICATION_READY,
    APPLICATION_READY,
    TRADE_PARTNER,
    LOGIN_READY,
    LOGGING_IN,
    ABORT_LIFE
}
