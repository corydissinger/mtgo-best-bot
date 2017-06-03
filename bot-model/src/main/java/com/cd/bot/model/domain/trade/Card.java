package com.cd.bot.model.domain.trade;

import com.cd.bot.model.domain.trade.NpcTradeableCard;
import com.cd.bot.model.domain.trade.OwnedTradeableCard;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;

/**
 * Created by Cory on 5/20/2017.
 */
@Entity
@JsonIgnoreProperties({ "ownedTradeableCards", "npcTradeableCards"})
public class Card {
    @Id
    @GeneratedValue
    private Long id;

    private String name;
    private String set;
    private Boolean premium;

    @ApiModelProperty(hidden = true)
    @OneToMany(mappedBy = "card")
    private List<OwnedTradeableCard> ownedTradeableCards;

    @ApiModelProperty(hidden = true)
    @OneToMany(mappedBy = "card")
    private List<NpcTradeableCard> npcTradeableCards;

    protected Card() {}

    public Card(String name, String set, Boolean premium) {
        this.name = name;
        this.set = set;
        this.premium = premium;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSet() {
        return set;
    }

    public void setSet(String set) {
        this.set = set;
    }

    public Boolean getPremium() {
        return premium;
    }

    public void setPremium(Boolean premium) {
        this.premium = premium;
    }

    public List<NpcTradeableCard> getNpcTradeableCards() {
        return npcTradeableCards;
    }

    public void setNpcTradeableCards(List<NpcTradeableCard> npcTradeableCards) {
        this.npcTradeableCards = npcTradeableCards;
    }

    public List<OwnedTradeableCard> getOwnedTradeableCards() {
        return ownedTradeableCards;
    }

    public void setOwnedTradeableCards(List<OwnedTradeableCard> ownedTradeableCards) {
        this.ownedTradeableCards = ownedTradeableCards;
    }
}
