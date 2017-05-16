package com.cd.bot.domain;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Cory on 5/16/2017.
 */
@Entity
public class BotCamera {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="bot_id")
    private Bot bot;

    @Lob
    @Column( name = "SCREEN_SHOT" )
    private byte[] screenShot;

    @Temporal(TemporalType.TIMESTAMP)
    private Date timeTaken;

    protected BotCamera() {}

    public BotCamera(byte[] screenShot, Date timeTaken) {
        this.screenShot = screenShot;
        this.timeTaken = timeTaken;
    }

    public Date getTimeTaken() {
        return timeTaken;
    }

    public void setTimeTaken(Date timeTaken) {
        this.timeTaken = timeTaken;
    }

    public byte[] getScreenShot() {
        return screenShot;
    }

    public void setScreenShot(byte[] screenShot) {
        this.screenShot = screenShot;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Bot getBot() {
        return bot;
    }

    public void setBot(Bot bot) {
        this.bot = bot;
    }
}
