package com.cd.bot.client.robot;

import com.cd.bot.api.domain.Bot;
import com.cd.bot.client.robot.exception.ApplicationDownException;
import com.cd.bot.client.robot.model.AssumedScreenTest;
import com.cd.bot.client.robot.model.ProcessingLifecycleStatus;
import com.cd.bot.client.robot.mtgo.ScreenConstants;
import com.cd.bot.client.service.BotCameraService;
import com.cd.bot.client.tesseract.RawLinesProcessor;
import com.cd.bot.client.tesseract.TesseractWrapper;
import com.cd.bot.client.tesseract.model.RawLines;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.awt.image.BufferedImage;

/**
 * Created by Cory on 5/13/2017.
 */
public class RobotMaster {

    private static final Logger logger = Logger.getLogger(RobotMaster.class);

    @Autowired
    private TesseractWrapper tesseractWrapper;

    @Autowired
    private RobotWrapper robotWrapper;

    @Autowired
    private RawLinesProcessor rawLinesProcessor;

    @Autowired
    private BotCameraService botCameraService;

    @Autowired
    private String botName;

    public void runBot() {
        Bot remoteBot = botCameraService.registerOrLoadSelf(new Bot(botName));

        ProcessingLifecycleStatus status = ProcessingLifecycleStatus.UNKNOWN;
        AssumedScreenTest screenTest = AssumedScreenTest.NOT_NEEDED;
        boolean shouldProcessScreen = false;

        do {
            logger.info("Current state is: " + status.name());

            BufferedImage bi = null;
            int sleepTime = 500;

            switch(status) {
                case APPLICATION_READY:
                    screenTest = AssumedScreenTest.COLLECTION_BOUNDS; //do some shits
                    shouldProcessScreen = true;
                    break;
                case TRADE_PARTNER:
                    screenTest = AssumedScreenTest.TRADE; //do some shits
                    shouldProcessScreen = true;
                    break;
                case UNKNOWN:
                    shouldProcessScreen = true;
                    break;
                case ACCEPT_TOS_EULA_READY:
                    final int yOffEnd = ScreenConstants.ACCEPT_TOS_SCROLL_TOP.getTop() + 400;

                    robotWrapper.clickAndDragVertical(ScreenConstants.ACCEPT_TOS_SCROLL_TOP.getLeft(),
                                                      ScreenConstants.ACCEPT_TOS_SCROLL_TOP.getTop(),
                                                      yOffEnd);

                    robotWrapper.doubleClickAtLocation(1115, 755);

                    status = ProcessingLifecycleStatus.UNKNOWN;

                    break;
                case LOGIN_READY:
                    robotWrapper.singleClickAtLocation(1235, 440);
                    robotWrapper.writeString("Nova4545");
                    robotWrapper.doubleClickAtLocation(ScreenConstants.LOGIN_BUTTON_CENTER.getLeft(), ScreenConstants.LOGIN_BUTTON_CENTER.getTop());

                    status = ProcessingLifecycleStatus.UNKNOWN;
                    sleepTime = 20000;
                    break;
            }

            if(shouldProcessScreen) {
                try {
                    bi = robotWrapper.getCurrentScreen(remoteBot);

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
