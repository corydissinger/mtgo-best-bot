package com.cd.bot.model.domain.trade;

import com.cd.bot.model.domain.PlayerBot;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

/**
 * Created by Cory on 5/20/2017.
 */
@Entity
@JsonIgnoreProperties("playerBot")
public class OwnedTradeableCard extends TradeableCard {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="bot_id")
    private PlayerBot playerBot;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="sell_order_id")
    private SellOrder sellOrder;

    public PlayerBot getPlayerBot() {
        return playerBot;
    }

    public void setPlayerBot(PlayerBot playerBot) {
        this.playerBot = playerBot;
    }

    public SellOrder getSellOrder() {
        return sellOrder;
    }

    public void setSellOrder(SellOrder sellOrder) {
        this.sellOrder = sellOrder;
    }
}
