package com.cd.bot.model.domain.bot;

import com.cd.bot.model.domain.BotCamera;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by Cory on 5/23/2017.
 */
@Entity
public class LifecycleEventOutcome implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    @OneToOne
    @JoinColumn(name="BOT_CAMERA_ID")
    private BotCamera botCamera;

    @OneToOne
    @JoinColumn(name="LIFECYCLE_EVENT_ID")
    private LifecycleEvent lifecycleEvent;

    private ProcessingLifecycleStatus processingLifecycleStatus;

    protected LifecycleEventOutcome() {}

    public LifecycleEventOutcome(BotCamera botCamera, ProcessingLifecycleStatus processingLifecycleStatus, LifecycleEvent lifecycleEvent) {
        this.botCamera = botCamera;
        this.processingLifecycleStatus = processingLifecycleStatus;
        this.lifecycleEvent = lifecycleEvent;
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

    public LifecycleEvent getLifecycleEvent() {
        return lifecycleEvent;
    }

    public void setLifecycleEvent(LifecycleEvent lifecycleEvent) {
        this.lifecycleEvent = lifecycleEvent;
    }
}
