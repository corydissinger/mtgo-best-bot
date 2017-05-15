package com.cd.bot.tesseract;

import com.cd.bot.robot.model.ProcessingLifecycleStatus;
import com.cd.bot.tesseract.model.RawLines;
import com.cd.bot.tesseract.model.RawWord;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;

/**
 * Created by Cory on 5/11/2017.
 */
public class RawLinesProcessor {
    public ProcessingLifecycleStatus determineLifecycleStatus(RawLines rawLines) {
        ProcessingLifecycleStatus status = ProcessingLifecycleStatus.UNKNOWN;
        List<List<RawWord>> lines = rawLines.getRawLines();

        if(CollectionUtils.isEmpty(lines)) {
            return status;
        }

        for(List<RawWord> line : lines) {
            if(status != ProcessingLifecycleStatus.UNKNOWN) {
                break;
            }

            for(RawWord word : line) {
                if(word.getWord().contains("Forgot") || word.getWord().contains("Password")) {
                    status = ProcessingLifecycleStatus.LOGIN_READY;
                    break;
                } else if(word.getWord().contains("Welcome")) {
                    status = ProcessingLifecycleStatus.APPLICATION_READY;
                    break;
                } else if(word.getWord().contains("Trade")) {
                    status = ProcessingLifecycleStatus.TRADE_PARTNER;
                }
            }
        }

        return status;
    }
}
