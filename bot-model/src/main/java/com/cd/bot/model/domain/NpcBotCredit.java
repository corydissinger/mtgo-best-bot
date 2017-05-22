package com.cd.bot.model.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;

/**
 * Created by Cory on 5/21/2017.
 */
@Entity
public class NpcBotCredit {
    @Id
    private Integer id;

    private Double credit;

    @OneToOne
    @MapsId
    private NpcBot npcBot;

    @OneToOne
    @MapsId
    private PlayerBot playerBot;

    protected NpcBotCredit() {}

    public NpcBotCredit(Integer id, NpcBot npcBot, PlayerBot playerBot, Double credit) {
        this.id = id;
        this.credit = credit;
        this.npcBot = npcBot;
        this.playerBot = playerBot;
    }

    public PlayerBot getPlayerBot() {
        return playerBot;
    }

    public void setPlayerBot(PlayerBot playerBot) {
        this.playerBot = playerBot;
    }

    public NpcBot getNpcBot() {
        return npcBot;
    }

    public void setNpcBot(NpcBot npcBot) {
        this.npcBot = npcBot;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getCredit() {
        return credit;
    }

    public void setCredit(Double credit) {
        this.credit = credit;
    }
}
