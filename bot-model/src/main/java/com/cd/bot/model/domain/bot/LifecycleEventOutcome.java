package com.cd.bot.model.domain.bot;

import com.cd.bot.model.domain.BotCamera;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

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

    @OneToMany(mappedBy = "lifecycleEventOutcome", fetch = FetchType.LAZY)
    private List<LifecycleEvent> lifecycleEvents;

    private ProcessingLifecycleStatus processingLifecycleStatus;

    protected LifecycleEventOutcome() {}

    public LifecycleEventOutcome(BotCamera botCamera, ProcessingLifecycleStatus processingLifecycleStatus) {
        this.botCamera = botCamera;
        this.processingLifecycleStatus = processingLifecycleStatus;
        this.lifecycleEvents = lifecycleEvents;
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

    public List<LifecycleEvent> getLifecycleEvents() {
        return lifecycleEvents;
    }

    public void setLifecycleEvents(List<LifecycleEvent> lifecycleEvents) {
        this.lifecycleEvents = lifecycleEvents;
    }
}
