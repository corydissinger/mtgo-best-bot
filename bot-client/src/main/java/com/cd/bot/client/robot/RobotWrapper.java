package com.cd.bot.client.robot;

import com.cd.bot.client.robot.exception.ApplicationDownException;
import com.cd.bot.client.system.ProcessManager;
import org.springframework.beans.factory.annotation.Autowired;

import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
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

    @Autowired
    private Boolean executableShortcutOnly;

    @Autowired
    private Integer executableXOffset;

    @Autowired
    private Integer executableYOffset;

    @Autowired
    private ProcessManager processManager;

    //private ScreenModel

    public BufferedImage getCurrentScreen() throws ApplicationDownException {
        if(!processManager.isMtgoRunningOrLoading()) {
            throw new ApplicationDownException();
        }

        BufferedImage image = robot.createScreenCapture(new Rectangle(0, 0, screenWidth, screenHeight));

        return image;
    }

    public void reInit() {
        if(executableShortcutOnly) {
            doubleClickAtLocation(executableXOffset, executableYOffset);
        } else {
            processManager.startApplication();
        }

        //Clean up any state we keep track of
    }

    public void singleClickAtLocation(int xOff, int yOff) {
        mouseEventAtLocation(InputEvent.BUTTON1_DOWN_MASK, xOff, yOff);
    }

    public void doubleClickAtLocation(int xOff, int yOff) {
        mouseEventAtLocation(InputEvent.BUTTON1_DOWN_MASK, xOff, yOff);
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        mouseEventAtLocation(InputEvent.BUTTON1_DOWN_MASK, xOff, yOff);
    }

    private void mouseEventAtLocation(int event, int xOff, int yOff) {
        robot.mouseMove(xOff, yOff);
        robot.mousePress(event);
        robot.mouseRelease(event);
    }

    public void writeString(String s) {
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (Character.isUpperCase(c)) {
                robot.keyPress(KeyEvent.VK_SHIFT);
            }
            robot.keyPress(Character.toUpperCase(c));
            robot.keyRelease(Character.toUpperCase(c));

            if (Character.isUpperCase(c)) {
                robot.keyRelease(KeyEvent.VK_SHIFT);
            }
        }
        robot.delay(700);
    }
}
