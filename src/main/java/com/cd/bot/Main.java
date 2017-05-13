package com.cd.bot;

import akka.actor.ActorRef;
import akka.actor.Inbox;
import com.cd.bot.akka.RobotActor;
import com.cd.bot.config.BotConfig;
import com.cd.bot.robot.RobotWrapper;
import com.cd.bot.robot.exception.ApplicationDownException;
import com.cd.bot.robot.model.AssumedScreenTest;
import com.cd.bot.robot.model.ProcessingLifecycleStatus;
import com.cd.bot.robot.mtgo.ScreenConstants;
import com.cd.bot.tesseract.RawLinesProcessor;
import com.cd.bot.tesseract.TesseractWrapper;
import com.cd.bot.tesseract.model.RawLines;
import com.cd.bot.tesseract.model.TesseractRectangle;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import scala.concurrent.duration.Duration;

import java.awt.image.BufferedImage;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class Main {

    private static final Logger logger = Logger.getLogger(Main.class);

    public static void main(String[] args) {
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(BotConfig.class);

        TesseractWrapper tesseractWrapper = (TesseractWrapper) applicationContext.getBean("tesseractWrapper");
        RobotWrapper robotWrapper = (RobotWrapper) applicationContext.getBean("robotWrapper");
        RawLinesProcessor rawLinesProcessor = (RawLinesProcessor) applicationContext.getBean("rawLinesProcessor");
        ActorRef robotRef = (ActorRef) applicationContext.getBean("robotRef");
        Inbox inbox = (Inbox) applicationContext.getBean("inbox");

        ProcessingLifecycleStatus status = ProcessingLifecycleStatus.UNKNOWN;
        AssumedScreenTest screenTest = AssumedScreenTest.NOT_NEEDED;
        boolean shouldProcessScreen = false;

        do {
            BufferedImage bi = null;
            int sleepTime = 500;

            switch(status) {
                case LOGGING_IN:
                    logger.info("Current state: LOGGING_IN");
                    screenTest = AssumedScreenTest.HOME_PAGE;
                case UNKNOWN:
                    logger.info("Current state: UNKNOWN");
                    try {
                        bi = robotWrapper.getCurrentScreen();
                        shouldProcessScreen = true;
                    } catch (ApplicationDownException e) {
                        e.printStackTrace();
                        robotWrapper.reInit();
                        screenTest = AssumedScreenTest.LOGIN;
                        sleepTime = 5000;
                    }
                    break;
                case LOGIN_READY:
                    logger.info("Current state: LOGIN_READY");
                    RobotActor.InputStringEvent e = new RobotActor.InputStringEvent();

                    e.setTextToInput("Nova4545");

                    inbox.send(robotRef, e);

                    try {
                        RobotActor.EventSuccessEvent resp = (RobotActor.EventSuccessEvent) inbox.receive(Duration.create(5, TimeUnit.SECONDS));

                        if(resp != null) {
                            RobotActor.MouseClickEvent me = new RobotActor.MouseClickEvent();

                            me.setDouble(true);
                            me.setxOffset(ScreenConstants.LOGIN_BUTTON_CENTER.getLeft());
                            me.setyOffset(ScreenConstants.LOGIN_BUTTON_CENTER.getTop());

                            inbox.send(robotRef, me);

                            RobotActor.ApplicationServerResponseAwaitEvent clickResp = (RobotActor.ApplicationServerResponseAwaitEvent) inbox.receive(Duration.create(5, TimeUnit.SECONDS));

                            if(clickResp != null) {
                                status = ProcessingLifecycleStatus.LOGGING_IN;

                            }
                        }
                    } catch (TimeoutException e1) {
                        e1.printStackTrace();
                        status = ProcessingLifecycleStatus.UNKNOWN;
                    }
                    break;
            }

            if(shouldProcessScreen) {
                RawLines rawLines;
                if(screenTest != AssumedScreenTest.NOT_NEEDED) {
                    rawLines = tesseractWrapper.getRawText(bi, screenTest.getScreenTestBounds());
                } else {
                    rawLines = tesseractWrapper.getRawText(bi);
                }

                logger.info("Processing raw lines");
                status = rawLinesProcessor.determineLifecycleStatus(rawLines);
                logger.info("Determined new status: " + status.name());
                shouldProcessScreen = false;
            }

            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
        } while (status != ProcessingLifecycleStatus.ABORT_LIFE);
    }

}
