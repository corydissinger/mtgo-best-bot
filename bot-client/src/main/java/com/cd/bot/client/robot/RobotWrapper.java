package com.cd.bot.client.robot;

import com.cd.bot.model.domain.PlayerBot;
import com.cd.bot.model.domain.BotCamera;
import com.cd.bot.client.robot.exception.ApplicationDownException;
import com.cd.bot.client.system.ProcessManager;
import com.cd.bot.wrapper.http.BotCameraService;
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

    static public class MouseClickEvent {
        private int xOffset;
        private int yOffset;
        private boolean isDouble;

        public int getxOffset() {
            return xOffset;
        }

        public void setxOffset(int xOffset) {
            this.xOffset = xOffset;
        }

        public int getyOffset() {
            return yOffset;
        }

        public void setyOffset(int yOffset) {
            this.yOffset = yOffset;
        }

        public boolean isDouble() {
            return isDouble;
        }

        public void setDouble(boolean aDouble) {
            isDouble = aDouble;
        }
    }

    static public class InputStringEvent {
        private String textToInput;

        public String getTextToInput() {
            return textToInput;
        }

        public void setTextToInput(String textToInput) {
            this.textToInput = textToInput;
        }
    }

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

    public BufferedImage getCurrentScreen(PlayerBot remotePlayerBot) throws ApplicationDownException {
        if(!processManager.isMtgoRunningOrLoading()) {
            throw new ApplicationDownException("MTGO is not running!");
        }

        BufferedImage image = robot.createScreenCapture(new Rectangle(0, 0, screenWidth, screenHeight));

        try {
            botCameraService.saveBotCam(createBotCamera(image, remotePlayerBot));
        } catch (IOException e) {
            throw new RuntimeException("Cannot communicate with API!");
        }

        return image;
    }

    private BotCamera createBotCamera(BufferedImage image, PlayerBot remotePlayerBot) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "jpg", baos );
        baos.flush();
        byte[] imageInByte = baos.toByteArray();

        BotCamera botCamera = new BotCamera(imageInByte, new Date());
        botCamera.setPlayerBot(remotePlayerBot);
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

    public void clickAndDragVertical(int xOffOrig, int yOffOrig, int yOffEnd) {
        final boolean isEndBelow = (yOffEnd > yOffOrig);
        final int distance = isEndBelow ? yOffEnd - yOffOrig : yOffOrig - yOffEnd;

        robot.mouseMove(xOffOrig, yOffOrig);
        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);

        int intervalForScreen = isEndBelow ? 20 : -20;

        for(int mouseMovePosition = yOffOrig;
                    isEndBelow ? mouseMovePosition < yOffEnd : mouseMovePosition > yOffEnd;
                    mouseMovePosition = getNextPosition(intervalForScreen, yOffEnd, mouseMovePosition)) {

            robot.mouseMove(xOffOrig, mouseMovePosition);
            robot.delay(20);
        }

        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
    }

    private int getNextPosition(int intervalForScreen, int yOffEnd, int currentPosition) {
        final int distanceLeft;

        if (intervalForScreen > 0) { //down town
            distanceLeft = yOffEnd - currentPosition;
        } else { //Or up
            distanceLeft = currentPosition - yOffEnd;
        }

        if(Math.abs(intervalForScreen) > distanceLeft) {
            return yOffEnd;
        } else {
            return currentPosition + intervalForScreen;
        }
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
            robot.delay(700);
        }
    }

    public void mouseWheel(int amount) {
        robot.mouseWheel(amount);
    }
}
