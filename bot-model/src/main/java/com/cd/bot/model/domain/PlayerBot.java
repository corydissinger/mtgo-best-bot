package com.cd.bot.model.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.util.List;

/**
 * Created by Cory on 5/16/2017.
 */
@Entity
@JsonIgnoreProperties({"botCameras", "botStatuses"})
public class PlayerBot extends Bot {
    @ApiModelProperty(hidden = true)
    @OneToMany(mappedBy = "playerBot")
    private List<BotStatus> botStatuses;

    @ApiModelProperty(hidden = true)
    @OneToMany(mappedBy = "playerBot")
    private List<BotCamera> botCameras;

    @ApiModelProperty(hidden = true)
    @OneToMany(mappedBy = "playerBot")
    private List<OwnedTradeableCard> botCards;

    @ApiModelProperty(hidden = true)
    @OneToMany(mappedBy = "playerBot")
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

    public List<BotStatus> getBotStatuses() {
        return botStatuses;
    }

    public void setBotStatuses(List<BotStatus> botStatuses) {
        this.botStatuses = botStatuses;
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
}
