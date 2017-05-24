package com.cd.bot.client.robot;

import com.cd.bot.client.model.AssumedScreenTest;
import com.cd.bot.client.model.LifecycleEvent;
import com.cd.bot.client.model.LifecycleEventOutcome;
import com.cd.bot.client.model.ProcessingLifecycleStatus;
import com.cd.bot.client.model.constant.ScreenConstants;
import com.cd.bot.client.model.exception.ApplicationDownException;
import com.cd.bot.client.tesseract.RawLines;
import com.cd.bot.client.tesseract.RawLinesProcessor;
import com.cd.bot.client.tesseract.TesseractWrapper;
import com.cd.bot.model.domain.BotCamera;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.awt.image.BufferedImage;
import java.io.IOException;

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
    private String botName;

    @Autowired
    private String password;

    public LifecycleEventOutcome runBot(LifecycleEvent lifecycleEvent) {
        LifecycleEventOutcome outcome = null;
        ProcessingLifecycleStatus status = lifecycleEvent.getProcessingLifecycleStatus();
        AssumedScreenTest screenTest = lifecycleEvent.getAssumedScreenTest();

        logger.info("Current state is: " + status.name());

        switch(status) {
            case APPLICATION_READY:
                screenTest = AssumedScreenTest.COLLECTION_BOUNDS; //do some shits
                break;
            case TRADE_PARTNER:
                screenTest = AssumedScreenTest.TRADE; //do some shits
                break;
            case UNKNOWN:
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
                robotWrapper.writeString(password);
                robotWrapper.doubleClickAtLocation(ScreenConstants.LOGIN_BUTTON_CENTER.getLeft(), ScreenConstants.LOGIN_BUTTON_CENTER.getTop());

                status = ProcessingLifecycleStatus.UNKNOWN;
                break;
        }

        try {
            outcome = robotWrapper.getCurrentScreen(status, screenTest);
        } catch (ApplicationDownException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(outcome == null) {
            return new LifecycleEventOutcome(null, ProcessingLifecycleStatus.UNKNOWN);
        }

        return outcome;
    }
}
