package com.cd.bot.api.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.util.List;

/**
 * Created by Cory on 5/16/2017.
 */
@Entity
@JsonIgnoreProperties({"botCameras", "botStatuses"})
public class Bot {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String name;

    @ApiModelProperty(hidden = true)
    @OneToMany(mappedBy = "bot")
    private List<BotStatus> botStatuses;

    @ApiModelProperty(hidden = true)
    @OneToMany(mappedBy = "bot")
    private List<BotCamera> botCameras;

    protected Bot() { }

    public Bot(String name) {
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
