package com.cd.bot.model.domain;

import javax.persistence.*;
import java.util.List;

/**
 * Created by Cory on 5/21/2017.
 */
@Entity
public class ExecutedTrade {
    @Id
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="npc_bot_id")
    private NpcBot npcBot;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="player_bot_id")
    private PlayerBot playerBot;

    @OneToMany(mappedBy = "executedTrade")
    private List<SellOrder> sellOrders;

    @OneToMany(mappedBy = "executedTrade")
    private List<SellOrder> npcTradedItems;
}
