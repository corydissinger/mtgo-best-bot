package com.cd.bot.client.model;

import com.cd.bot.model.domain.BotCamera;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * Created by Cory on 5/23/2017.
 */
public class LifecycleEventOutcome implements Serializable {
    private BotCamera botCamera;
    private ProcessingLifecycleStatus processingLifecycleStatus;

    public LifecycleEventOutcome() {}

    public LifecycleEventOutcome(ProcessingLifecycleStatus processingLifecycleStatus) {
        this.processingLifecycleStatus = processingLifecycleStatus;
        this.botCamera = null;
    }

    public LifecycleEventOutcome(BotCamera botCamera, ProcessingLifecycleStatus processingLifecycleStatus) {
        this.botCamera = botCamera;
        this.processingLifecycleStatus = processingLifecycleStatus;
    }

    public BotCamera getBotCamera() {
        return botCamera;
    }

    public void setBotCamera(BotCamera botCamera) {
        this.botCamera = botCamera;
    }

    public ProcessingLifecycleStatus getProcessingLifecycleStatus() {
        return processingLifecycleStatus;
    }

    public void setProcessingLifecycleStatus(ProcessingLifecycleStatus processingLifecycleStatus) {
        this.processingLifecycleStatus = processingLifecycleStatus;
    }
}
