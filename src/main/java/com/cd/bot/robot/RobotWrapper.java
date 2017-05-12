package com.cd.bot.robot;

import org.springframework.beans.factory.annotation.Autowired;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by Cory on 5/11/2017.
 */
public class RobotWrapper {
    @Autowired
    private Robot robot;

    @Autowired
    private Integer screenWidth;

    @Autowired
    private Integer screenHeight;

    public BufferedImage getCurrentScreen() {
        return robot.createScreenCapture(new Rectangle(0, 0, screenWidth, screenHeight));
    }
}
