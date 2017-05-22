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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="executed_trade_id")
    private ExecutedTrade executedTrade;

    @ManyToMany(targetEntity = OwnedTradeableCard.class)
    private List<OwnedTradeableCard> ownedTradeableCards;

    protected SellOrder() {}

    public SellOrder(Long id, Date timeTaken, ExecutedTrade executedTrade, List<OwnedTradeableCard> ownedTradeableCards) {
        this.id = id;
        this.timeTaken = timeTaken;
        this.executedTrade = executedTrade;
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

    public ExecutedTrade getExecutedTrade() {
        return executedTrade;
    }

    public void setExecutedTrade(ExecutedTrade executedTrade) {
        this.executedTrade = executedTrade;
    }

    public List<OwnedTradeableCard> getOwnedTradeableCards() {
        return ownedTradeableCards;
    }

    public void setOwnedTradeableCards(List<OwnedTradeableCard> ownedTradeableCards) {
        this.ownedTradeableCards = ownedTradeableCards;
    }
}
