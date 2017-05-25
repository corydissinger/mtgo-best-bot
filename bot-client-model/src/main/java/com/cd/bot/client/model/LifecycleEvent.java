package com.cd.bot.client.model;

import java.io.Serializable;

/**
 * Created by Cory on 5/23/2017.
 */
public class LifecycleEvent implements Serializable {
    private AssumedScreenTest assumedScreenTest;
    private ProcessingLifecycleStatus processingLifecycleStatus;

    public LifecycleEvent() {}

    public LifecycleEvent(AssumedScreenTest assumedScreenTest, ProcessingLifecycleStatus processingLifecycleStatus) {
        this.assumedScreenTest = assumedScreenTest;
        this.processingLifecycleStatus = processingLifecycleStatus;
    }

    public AssumedScreenTest getAssumedScreenTest() {
        return assumedScreenTest;
    }

    public void setAssumedScreenTest(AssumedScreenTest assumedScreenTest) {
        this.assumedScreenTest = assumedScreenTest;
    }

    public ProcessingLifecycleStatus getProcessingLifecycleStatus() {
        return processingLifecycleStatus;
    }

    public void setProcessingLifecycleStatus(ProcessingLifecycleStatus processingLifecycleStatus) {
        this.processingLifecycleStatus = processingLifecycleStatus;
    }
}
