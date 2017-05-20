package com.cd.bot.model.domain;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Cory on 5/15/2017.
 */
@Entity
public class BotStatus {

    @Id
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="bot_id")
    private PlayerBot playerBot;

    @Temporal(TemporalType.TIMESTAMP)
    private Date timeTaken;

    @Column(nullable = false)
    private Integer lastKnownTicketsOnHand;

    protected BotStatus() {}

    public BotStatus(Long id, PlayerBot playerBot, Integer lastKnownTicketsOnHand) {
        this.id = id;
        this.playerBot = playerBot;
        this.lastKnownTicketsOnHand = lastKnownTicketsOnHand;
    }

    public Integer getLastKnownTicketsOnHand() {
        return lastKnownTicketsOnHand;
    }

    public void setLastKnownTicketsOnHand(Integer lastKnownTicketsOnHand) {
        this.lastKnownTicketsOnHand = lastKnownTicketsOnHand;
    }

    public PlayerBot getPlayerBot() {
        return playerBot;
    }

    public void setPlayerBot(PlayerBot playerBot) {
        this.playerBot = playerBot;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
