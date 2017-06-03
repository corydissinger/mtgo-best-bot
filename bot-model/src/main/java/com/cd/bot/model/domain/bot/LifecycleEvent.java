package com.cd.bot.model.domain.bot;

import com.cd.bot.model.domain.PlayerBot;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by Cory on 5/23/2017.
 */
@Entity
public class LifecycleEvent implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    private AssumedScreenTest assumedScreenTest;
    private ProcessingLifecycleStatus processingLifecycleStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="bot_id")
    private PlayerBot playerBot;    
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date timeRequested;

    @Temporal(TemporalType.TIMESTAMP)
    private Date timeExecuted;

    protected LifecycleEvent() {}

    public LifecycleEvent(AssumedScreenTest assumedScreenTest, ProcessingLifecycleStatus processingLifecycleStatus, PlayerBot playerBot, Date timeRequested) {
        this.assumedScreenTest = assumedScreenTest;
        this.processingLifecycleStatus = processingLifecycleStatus;
        this.playerBot = playerBot;
        this.timeRequested = timeRequested;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Date getTimeRequested() {
        return timeRequested;
    }

    public void setTimeRequested(Date timeRequested) {
        this.timeRequested = timeRequested;
    }

    public Date getTimeExecuted() {
        return timeExecuted;
    }

    public void setTimeExecuted(Date timeExecuted) {
        this.timeExecuted = timeExecuted;
    }

    public PlayerBot getPlayerBot() {
        return playerBot;
    }

    public void setPlayerBot(PlayerBot playerBot) {
        this.playerBot = playerBot;
    }
}
