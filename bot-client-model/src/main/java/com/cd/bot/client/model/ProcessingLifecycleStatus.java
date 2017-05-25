package com.cd.bot.client.model;

/**
 * Created by Cory on 5/13/2017.
 */
public enum ProcessingLifecycleStatus {
    UNKNOWN,
    AWAIT_APPLICATION_READY,
    APPLICATION_DOWN,
    APPLICATION_READY,
    TRADE_PARTNER,
    ACCEPT_TOS_EULA_READY,
    LOGIN_READY,
    LOGGING_IN,
    ABORT_LIFE,
    APPLICATION_START
}
