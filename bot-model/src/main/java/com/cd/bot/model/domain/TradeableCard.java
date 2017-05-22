package com.cd.bot.model.domain;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Cory on 5/20/2017.
 */
@MappedSuperclass
public abstract class TradeableCard {
    protected Double price;
    protected Integer quantity;

    @Id
    @GeneratedValue
    protected Long id;

    @Temporal(TemporalType.TIMESTAMP)
    protected Date asOf;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="card_name")
    protected Card card;

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    public Date getAsOf() {
        return asOf;
    }

    public void setAsOf(Date asOf) {
        this.asOf = asOf;
    }
}
