package com.cd.bot.domain;

import javax.persistence.*;
import java.util.List;

/**
 * Created by Cory on 5/16/2017.
 */
@Entity
public class Bot {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "bot")
    private List<BotStatus> botStatuses;

    @OneToMany(mappedBy = "bot")
    private List<BotCamera> botCameras;

    protected Bot() { }

    public Bot(Long id, String name) {
        this.id = id;
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
}
