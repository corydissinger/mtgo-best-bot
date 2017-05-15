package com.cd.bot.domain;

import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created by Cory on 5/15/2017.
 */
@Entity
public class BotStatus {

    @Id
    private String botName;

    @Column(nullable = false)
    private Integer lastKnownTicketsOnHand;

    protected BotStatus() {}

    public BotStatus(String botName, Integer lastKnownTicketsOnHand) {
        this.botName = botName;
        this.lastKnownTicketsOnHand = lastKnownTicketsOnHand;
    }

    @ApiModelProperty(notes = "The last known amount of tickets for this bot", required = true)
    public Integer getLastKnownTicketsOnHand() {
        return lastKnownTicketsOnHand;
    }

    public void setLastKnownTicketsOnHand(Integer lastKnownTicketsOnHand) {
        this.lastKnownTicketsOnHand = lastKnownTicketsOnHand;
    }

    @ApiModelProperty(notes = "The username of the bot", required = true)
    public String getBotName() {
        return botName;
    }

    public void setBotName(String botName) {
        this.botName = botName;
    }
}
