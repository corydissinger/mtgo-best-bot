package com.cd.bot.tesseract;

import com.cd.bot.robot.model.ProcessingLifecycleStatus;
import com.cd.bot.tesseract.model.RawLines;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;

/**
 * Created by Cory on 5/11/2017.
 */
public class RawLinesProcessor {
    public ProcessingLifecycleStatus determineLifecycleStatus(RawLines rawLines) {
        ProcessingLifecycleStatus status = ProcessingLifecycleStatus.UNKNOWN;
        List<String> lines = rawLines.getRawLines();

        if(CollectionUtils.isEmpty(lines)) {
            return status;
        }

        for(String line : lines) {
            if(line.contains("Forgot Password") || line.contains("LOG IN")) {
                status = ProcessingLifecycleStatus.LOGIN_READY;
                break;
            } else if(line.contains("Welcome")) {
                status = ProcessingLifecycleStatus.APPLICATION_READY;
                break;
            }
        }

        return status;
    }
}
