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
import org.apache.commons.lang3.tuple.Pair;
import org.apache.log4j.Logger;
import org.omg.CORBA.UNKNOWN;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.BlockingQueue;

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

    @Autowired
    private BlockingQueue<LifecycleEvent> processingEventsQueue;

    public void runBot(LifecycleEvent lifecycleEvent) {
        ProcessingLifecycleStatus status = lifecycleEvent.getProcessingLifecycleStatus();
        AssumedScreenTest screenTest = AssumedScreenTest.NOT_NEEDED;

        logger.info("Current state is: " + status.name());

        switch(status) {
            case APPLICATION_READY:
                screenTest = AssumedScreenTest.COLLECTION_BOUNDS; //do some shits
                break;
            case TRADE_PARTNER:
                screenTest = AssumedScreenTest.TRADE; //do some shits
                break;
            case UNKNOWN:
                screenTest = AssumedScreenTest.EULA;
                break;
            case DETERMINE_LOGIN_READY:
                screenTest = AssumedScreenTest.LOGIN;
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

        BotCamera botCamera;
        ProcessingLifecycleStatus currentScreenOutcomeStatus;

        try {
            Pair<ProcessingLifecycleStatus, byte[]> statusToImageResult = robotWrapper.getCurrentScreen(status, screenTest);
            botCamera = new BotCamera(statusToImageResult.getRight(), new Date());
            currentScreenOutcomeStatus = statusToImageResult.getLeft();
        } catch (ApplicationDownException e) {
            logger.error(e.getMessage());
            botCamera = new BotCamera(new byte[0], new Date());
            currentScreenOutcomeStatus = ProcessingLifecycleStatus.APPLICATION_DOWN;
        } catch (IOException e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }

        botCameraRepository.save(botCamera);

        if(ProcessingLifecycleStatus.APPLICATION_READY == currentScreenOutcomeStatus) {
            final LifecycleEventOutcome outcome = new LifecycleEventOutcome(botCamera, currentScreenOutcomeStatus);
            lifecycleEvent.setLifecycleEventOutcome(outcome);
            lifecycleEventOutcomeRepository.save(outcome);
            lifecycleEventRepository.save(lifecycleEvent);
        } else {
            LifecycleEvent nextAutomaticEvent = new LifecycleEvent(getNextAutomaticStatus(currentScreenOutcomeStatus, screenTest), lifecycleEvent.getPlayerBot(), new Date());
            nextAutomaticEvent.setAutomatic(true);
            lifecycleEventRepository.save(nextAutomaticEvent);

            try {
                processingEventsQueue.put(nextAutomaticEvent);
            } catch (InterruptedException e) {
                logger.error(e.getMessage());
            }
        }
    }

    private ProcessingLifecycleStatus getNextAutomaticStatus(ProcessingLifecycleStatus priorStatus, AssumedScreenTest priorScreenTest) {
        switch(priorStatus) {
            case APPLICATION_DOWN:
                return ProcessingLifecycleStatus.APPLICATION_START;
            case APPLICATION_START:
                return ProcessingLifecycleStatus.UNKNOWN;
            case UNKNOWN:
                return getNextAutomaticStatusFromUnknown(priorScreenTest);
            default:
                return priorStatus;
        }
    }

    private ProcessingLifecycleStatus getNextAutomaticStatusFromUnknown(AssumedScreenTest priorScreenTest) {
        switch (priorScreenTest) {
            case NOT_NEEDED:
                return ProcessingLifecycleStatus.DETERMINE_LOGIN_READY;
            case EULA:
                return ProcessingLifecycleStatus.DETERMINE_LOGIN_READY;
            default:
                return ProcessingLifecycleStatus.ABORT_LIFE;
        }
    }
}
