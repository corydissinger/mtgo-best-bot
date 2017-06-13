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

    private ProcessingLifecycleStatus processingLifecycleStatus;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="bot_id")
    private PlayerBot playerBot;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "lifecycleEventOutcome")
    @JoinColumn(nullable = true)
    private LifecycleEventOutcome lifecycleEventOutcome;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date timeRequested;

    protected LifecycleEvent() {}

    public LifecycleEvent(ProcessingLifecycleStatus processingLifecycleStatus, PlayerBot playerBot, Date timeRequested) {
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

    public PlayerBot getPlayerBot() {
        return playerBot;
    }

    public void setPlayerBot(PlayerBot playerBot) {
        this.playerBot = playerBot;
    }

    public LifecycleEventOutcome getLifecycleEventOutcome() {
        return lifecycleEventOutcome;
    }

    public void setLifecycleEventOutcome(LifecycleEventOutcome lifecycleEventOutcome) {
        this.lifecycleEventOutcome = lifecycleEventOutcome;
    }
}
