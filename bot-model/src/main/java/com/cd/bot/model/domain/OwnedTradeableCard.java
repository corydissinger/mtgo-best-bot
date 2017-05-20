package com.cd.bot.model.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

/**
 * Created by Cory on 5/20/2017.
 */
@Entity
public class OwnedTradeableCard extends TradeableCard {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="bot_id")
    private PlayerBot playerBot;

    public PlayerBot getPlayerBot() {
        return playerBot;
    }

    public void setPlayerBot(PlayerBot playerBot) {
        this.playerBot = playerBot;
    }
}
