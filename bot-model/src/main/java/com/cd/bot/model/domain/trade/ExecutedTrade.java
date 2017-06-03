package com.cd.bot.model.domain.trade;

import com.cd.bot.model.domain.NpcBot;
import com.cd.bot.model.domain.PlayerBot;

import javax.persistence.*;

/**
 * Created by Cory on 5/21/2017.
 */
@Entity
public class ExecutedTrade {
    @Id
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name="npc_bot_id")
    private NpcBot npcBot;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="player_bot_id")
    private PlayerBot playerBot;

    @OneToOne @MapsId
    private SellOrder sellOrder;

    @OneToOne @MapsId
    private BuyOrder buyOrder;

    protected ExecutedTrade() {}

    public ExecutedTrade(PlayerBot playerBot, NpcBot npcBot, SellOrder sellOrder, BuyOrder buyOrder) {
        this.npcBot = npcBot;
        this.playerBot = playerBot;
        this.sellOrder = sellOrder;
        this.buyOrder = buyOrder;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public NpcBot getNpcBot() {
        return npcBot;
    }

    public void setNpcBot(NpcBot npcBot) {
        this.npcBot = npcBot;
    }

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

    public BuyOrder getBuyOrder() {
        return buyOrder;
    }

    public void setBuyOrder(BuyOrder buyOrder) {
        this.buyOrder = buyOrder;
    }
}
