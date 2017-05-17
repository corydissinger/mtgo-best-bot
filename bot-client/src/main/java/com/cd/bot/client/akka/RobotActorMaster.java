package com.cd.bot.client.akka;

import akka.actor.ActorRef;
import akka.actor.Inbox;
import com.cd.bot.client.robot.RobotWrapper;
import com.cd.bot.client.robot.exception.ApplicationDownException;
import com.cd.bot.client.robot.model.AssumedScreenTest;
import com.cd.bot.client.robot.model.ProcessingLifecycleStatus;
import com.cd.bot.client.robot.mtgo.ScreenConstants;
import com.cd.bot.client.tesseract.RawLinesProcessor;
import com.cd.bot.client.tesseract.TesseractWrapper;
import com.cd.bot.client.tesseract.model.RawLines;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import scala.concurrent.duration.Duration;

import java.awt.image.BufferedImage;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by Cory on 5/13/2017.
 */
public class RobotActorMaster {

    private static final Logger logger = Logger.getLogger(RobotActorMaster.class);

    @Autowired
    private TesseractWrapper tesseractWrapper;

    @Autowired
    private RobotWrapper robotWrapper;

    @Autowired
    private RawLinesProcessor rawLinesProcessor;

    @Autowired
    private ActorRef robotRef;

    @Autowired
    private Inbox inbox;

    public void runBot() {
        ProcessingLifecycleStatus status = ProcessingLifecycleStatus.UNKNOWN;
        AssumedScreenTest screenTest = AssumedScreenTest.NOT_NEEDED;
        boolean shouldProcessScreen = false;

        do {
            BufferedImage bi = null;
            int sleepTime = 500;

            switch(status) {
                case APPLICATION_READY:
                    logger.info("Current state: APPLICATION_READY");
                    screenTest = AssumedScreenTest.COLLECTION_BOUNDS; //do some shits
                    shouldProcessScreen = true;
                    break;
                case TRADE_PARTNER:
                    logger.info("Current state: TRADE_PARTNER");
                    screenTest = AssumedScreenTest.TRADE; //do some shits
                    shouldProcessScreen = true;
                    break;
                case UNKNOWN:
                    logger.info("Current state: UNKNOWN");
                    shouldProcessScreen = true;
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
                        }
                    } catch (TimeoutException e1) {
                        e1.printStackTrace();
                    }

                    status = ProcessingLifecycleStatus.UNKNOWN;
                    sleepTime = 20000;
                    break;
            }

            if(shouldProcessScreen) {
                try {
                    bi = robotWrapper.getCurrentScreen();

                    if(status == ProcessingLifecycleStatus.UNKNOWN) {
                        if(screenTest == AssumedScreenTest.HOME_PAGE) {
                            screenTest = AssumedScreenTest.LOGIN;
                        } else {
                            screenTest = AssumedScreenTest.HOME_PAGE;
                        }
                    }
                } catch (ApplicationDownException e) {
                    e.printStackTrace();
                    robotWrapper.reInit();
                    screenTest = AssumedScreenTest.LOGIN;
                    sleepTime = 5000;
                }

                if(bi != null) {
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
            }

            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
        } while (status != ProcessingLifecycleStatus.ABORT_LIFE);
    }
}
