package com.cd.bot.model.domain;

import com.cd.bot.model.domain.trade.ExecutedTrade;
import com.cd.bot.model.domain.trade.NpcTradeableCard;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.List;

/**
 * Created by Cory on 5/20/2017.
 */
@Entity
public class NpcBot extends Bot {

    @ApiModelProperty(hidden = true)
    @OneToMany(mappedBy = "npcBot")
    private List<NpcTradeableCard> npcTradeableCards;

    @OneToMany(mappedBy = "npcBot")
    private List<ExecutedTrade> executedTrades;

    protected NpcBot() {}

    public NpcBot(String name) {
        this.name = name;
    }

    public List<NpcTradeableCard> getNpcTradeableCards() {
        return npcTradeableCards;
    }

    public void setNpcTradeableCards(List<NpcTradeableCard> npcTradeableCards) {
        this.npcTradeableCards = npcTradeableCards;
    }

    public List<ExecutedTrade> getExecutedTrades() {
        return executedTrades;
    }

    public void setExecutedTrades(List<ExecutedTrade> executedTrades) {
        this.executedTrades = executedTrades;
    }
}
