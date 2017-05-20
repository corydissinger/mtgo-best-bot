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
    private Bot bot;

    @Temporal(TemporalType.TIMESTAMP)
    private Date timeTaken;

    @Column(nullable = false)
    private Integer lastKnownTicketsOnHand;

    protected BotStatus() {}

    public BotStatus(Long id, Bot bot, Integer lastKnownTicketsOnHand) {
        this.id = id;
        this.bot = bot;
        this.lastKnownTicketsOnHand = lastKnownTicketsOnHand;
    }

    public Integer getLastKnownTicketsOnHand() {
        return lastKnownTicketsOnHand;
    }

    public void setLastKnownTicketsOnHand(Integer lastKnownTicketsOnHand) {
        this.lastKnownTicketsOnHand = lastKnownTicketsOnHand;
    }

    public Bot getBot() {
        return bot;
    }

    public void setBot(Bot bot) {
        this.bot = bot;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
