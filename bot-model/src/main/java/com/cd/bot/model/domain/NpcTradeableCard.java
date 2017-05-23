package com.cd.bot.model.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

/**
 * Created by Cory on 5/20/2017.
 */
@Entity
public class NpcTradeableCard extends TradeableCard {

    protected NpcTradeableCard() {}

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="bot_id")
    private NpcBot npcBot;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="buy_order_id")
    private BuyOrder buyOrder;

    public NpcBot getPlayerBot() {
        return npcBot;
    }

    public void setPlayerBot(NpcBot npcBot) {
        this.npcBot = npcBot;
    }

    public BuyOrder getBuyOrder() {
        return buyOrder;
    }

    public void setBuyOrder(BuyOrder buyOrder) {
        this.buyOrder = buyOrder;
    }
}
