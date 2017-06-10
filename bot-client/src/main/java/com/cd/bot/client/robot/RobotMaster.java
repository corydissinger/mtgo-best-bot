package com.cd.bot.client.robot;

import com.cd.bot.model.domain.bot.AssumedScreenTest;
import com.cd.bot.model.domain.bot.LifecycleEvent;
import com.cd.bot.model.domain.bot.LifecycleEventOutcome;
import com.cd.bot.model.domain.bot.ProcessingLifecycleStatus;
import com.cd.bot.model.domain.bot.constant.ScreenConstants;
import com.cd.bot.model.domain.bot.exception.ApplicationDownException;
import com.cd.bot.client.system.ProcessManager;
import com.cd.bot.client.tesseract.RawLinesProcessor;
import com.cd.bot.client.tesseract.TesseractWrapper;
import com.cd.bot.model.domain.BotCamera;
import com.cd.bot.model.domain.repository.BotCameraRepository;
import com.cd.bot.model.domain.repository.LifecycleEventOutcomeRepository;
import com.cd.bot.model.domain.repository.LifecycleEventRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.util.Date;

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
    private ProcessManager processManager;

    @Autowired
    private String botName;

    @Autowired
    private String password;

    @Value("${kafka.topic.outcome}")
    private String outcomeTopic;

    @Autowired
    private LifecycleEventRepository lifecycleEventRepository;

    @Autowired
    private LifecycleEventOutcomeRepository lifecycleEventOutcomeRepository;

    @Autowired
    private BotCameraRepository botCameraRepository;

    public void runBot(LifecycleEvent lifecycleEvent) {
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
            case APPLICATION_START:
                if(!processManager.isMtgoRunningOrLoading()) {
                    robotWrapper.reInit();
                }

                break;
        }

        try {
            outcome = robotWrapper.getCurrentScreen(lifecycleEvent);
        } catch (ApplicationDownException e) {
            logger.error(e.getMessage());
            outcome = new LifecycleEventOutcome(new BotCamera(new byte[0], new Date()), ProcessingLifecycleStatus.APPLICATION_DOWN, lifecycleEvent);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(outcome == null) {
            outcome = new LifecycleEventOutcome(new BotCamera(new byte[0], new Date()), ProcessingLifecycleStatus.UNKNOWN, lifecycleEvent);
        }

        botCameraRepository.save(outcome.getBotCamera());
        lifecycleEventOutcomeRepository.save(outcome);
    }
}
