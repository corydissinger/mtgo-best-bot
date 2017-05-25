package com.cd.bot.client.robot;

import com.cd.bot.client.config.BotConfig;
import com.cd.bot.client.model.AssumedScreenTest;
import com.cd.bot.client.model.LifecycleEventOutcome;
import com.cd.bot.client.model.ProcessingLifecycleStatus;
import com.cd.bot.client.model.exception.ApplicationDownException;
import com.cd.bot.client.system.ProcessManager;
import com.cd.bot.client.tesseract.RawLines;
import com.cd.bot.client.tesseract.RawLinesProcessor;
import com.cd.bot.client.tesseract.TesseractWrapper;
import com.cd.bot.model.domain.BotCamera;
import com.cd.bot.model.domain.PlayerBot;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.log4j.Logger;
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

    private static final Logger logger = Logger.getLogger(RobotWrapper.class);

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
    private TesseractWrapper tesseractWrapper;

    @Autowired
    private RawLinesProcessor rawLinesProcessor;

    public LifecycleEventOutcome getCurrentScreen(ProcessingLifecycleStatus status, AssumedScreenTest screenTest) throws ApplicationDownException, IOException {
        if(!processManager.isMtgoRunningOrLoading()) {
            throw new ApplicationDownException("MTGO is not running!");
        }

        BufferedImage bi = robot.createScreenCapture(new Rectangle(0, 0, screenWidth, screenHeight));
        //TODO - MULTI SCREEN STATE TEST BOIY
        if(status == ProcessingLifecycleStatus.UNKNOWN) {
            if(screenTest == AssumedScreenTest.EULA) {
                screenTest = AssumedScreenTest.HOME_PAGE;
            } if(screenTest == AssumedScreenTest.HOME_PAGE) {
                screenTest = AssumedScreenTest.LOGIN;
            } else {
                screenTest = AssumedScreenTest.EULA;
            }
        }

        if(bi != null) {
            RawLines rawLines;

            if (screenTest != AssumedScreenTest.NOT_NEEDED) {
                rawLines = tesseractWrapper.getRawText(bi, screenTest.getScreenTestBounds());
            } else {
                rawLines = tesseractWrapper.getRawText(bi);
            }

            logger.info("Processing raw lines");
            status = rawLinesProcessor.determineLifecycleStatus(rawLines);
            logger.info("Determined new status: " + status.name());

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(bi, "jpg", baos);
            baos.flush();
            byte[] imageAsByteArray = baos.toByteArray();
            baos.close();

            BotCamera botCamera = new BotCamera(imageAsByteArray, new Date());

            return new LifecycleEventOutcome(botCamera, status);

        }

        throw new ApplicationDownException("Somehow made it to this unreachable point");
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
