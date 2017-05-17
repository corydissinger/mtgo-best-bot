package com.cd.bot.client.robot;

import com.cd.bot.api.domain.Bot;
import com.cd.bot.api.domain.BotCamera;
import com.cd.bot.client.robot.exception.ApplicationDownException;
import com.cd.bot.client.service.BotCameraService;
import com.cd.bot.client.system.ProcessManager;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.springframework.beans.factory.annotation.Autowired;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Date;

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

    @Autowired
    private BotCameraService botCameraService;

    //private ScreenModel

    public BufferedImage getCurrentScreen(Bot remoteBot) throws ApplicationDownException {
        if(!processManager.isMtgoRunningOrLoading()) {
            throw new ApplicationDownException("MTGO is not running!");
        }

        BufferedImage image = robot.createScreenCapture(new Rectangle(0, 0, screenWidth, screenHeight));

        try {
            botCameraService.saveBotCam(createBotCamera(image, remoteBot));
        } catch (IOException e) {
            throw new ApplicationDownException("Cannot communicate with API!");
        }

        return image;
    }

    private BotCamera createBotCamera(BufferedImage image, Bot remoteBot) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "jpg", baos );
        baos.flush();
        byte[] imageInByte = baos.toByteArray();

        BotCamera botCamera = new BotCamera(imageInByte, new Date());
        botCamera.setBot(remoteBot);
        return botCamera;
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
