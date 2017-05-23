package com.cd.bot.model.domain;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Created by Cory on 5/21/2017.
 */
@Entity
public class SellOrder {
    @Id
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    private Date timeTaken;

    @OneToMany(mappedBy = "sellOrder")
    private List<OwnedTradeableCard> ownedTradeableCards;

    protected SellOrder() {}

    public SellOrder(Date timeTaken, List<OwnedTradeableCard> ownedTradeableCards) {
        this.timeTaken = timeTaken;
        this.ownedTradeableCards = ownedTradeableCards;
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

    public List<OwnedTradeableCard> getOwnedTradeableCards() {
        return ownedTradeableCards;
    }

    public void setOwnedTradeableCards(List<OwnedTradeableCard> ownedTradeableCards) {
        this.ownedTradeableCards = ownedTradeableCards;
    }
}
