package com.cd.bot.model.domain;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Created by Cory on 5/21/2017.
 */
@Entity
public class BuyOrder {
    @Id
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    private Date timeTaken;

    @OneToMany(mappedBy = "buyOrder")
    private List<NpcTradeableCard> npcTradeableCards;

    protected BuyOrder() {}

    public BuyOrder(Date timeTaken, List<NpcTradeableCard> npcTradeableCards) {
        this.timeTaken = timeTaken;
        this.npcTradeableCards = npcTradeableCards;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getTimeTaken() {
        return timeTaken;
    }

    public void setTimeTaken(Date timeTaken) {
        this.timeTaken = timeTaken;
    }

    public List<NpcTradeableCard> getNpcTradeableCards() {
        return npcTradeableCards;
    }

    public void setNpcTradeableCards(List<NpcTradeableCard> npcTradeableCards) {
        this.npcTradeableCards = npcTradeableCards;
    }
}
