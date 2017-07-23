package com.cd.bot.model.domain;

import com.cd.bot.model.domain.trade.ExecutedTrade;
import com.cd.bot.model.domain.trade.OwnedTradeableCard;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.List;

/**
 * Created by Cory on 5/16/2017.
 */
@Entity
@JsonIgnoreProperties({"botCameras", "botStatuses"})
public class PlayerBot extends Bot {
    @Transient
    private String status;

    @OneToMany(mappedBy = "playerBot", fetch = FetchType.LAZY)
    private List<BotCamera> botCameras;

    @OneToMany(mappedBy = "playerBot", fetch = FetchType.LAZY)
    private List<OwnedTradeableCard> botCards;

    @OneToMany(mappedBy = "playerBot", fetch = FetchType.LAZY)
    private List<ExecutedTrade> executedTrades;

    protected PlayerBot() { }

    public PlayerBot(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<BotCamera> getBotCameras() {
        return botCameras;
    }

    public void setBotCameras(List<BotCamera> botCameras) {
        this.botCameras = botCameras;
    }

    public List<OwnedTradeableCard> getBotCards() {
        return botCards;
    }

    public void setBotCards(List<OwnedTradeableCard> botCards) {
        this.botCards = botCards;
    }

    public List<ExecutedTrade> getExecutedTrades() {
        return executedTrades;
    }

    public void setExecutedTrades(List<ExecutedTrade> executedTrades) {
        this.executedTrades = executedTrades;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
