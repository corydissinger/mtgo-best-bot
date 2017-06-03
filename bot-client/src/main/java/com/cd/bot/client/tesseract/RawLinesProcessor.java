package com.cd.bot.client.tesseract;

import com.cd.bot.model.domain.bot.ProcessingLifecycleStatus;
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
                } else if(word.getWord().contains("SOFTWARE")) {
                    status = ProcessingLifecycleStatus.ACCEPT_TOS_EULA_READY;
                }
            }
        }

        return status;
    }
}
