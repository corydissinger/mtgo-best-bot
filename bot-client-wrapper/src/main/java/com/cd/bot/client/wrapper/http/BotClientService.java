package com.cd.bot.wrapper.http;

import com.cd.bot.client.model.AssumedScreenTest;
import com.cd.bot.client.model.LifecycleEvent;
import com.cd.bot.client.model.LifecycleEventOutcome;
import com.cd.bot.client.model.ProcessingLifecycleStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

/**
 * Created by Cory on 5/16/2017.
 */
public class BotClientService {

    private static final Logger log = LoggerFactory.getLogger(BotClientService.class);

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private String botClientUrl;

    public LifecycleEventOutcome sendLifecycleEvent(LifecycleEvent event) {
        ResponseEntity<LifecycleEventOutcome> resp;

        try {
            resp = restTemplate.postForEntity(botClientUrl + "/client-bot", event, LifecycleEventOutcome.class);
        } catch (RestClientException e) {
            log.error("Failed to log in!");
            log.error(e.getMessage());
            throw new RuntimeException();
        }


        if(resp.getStatusCode() != HttpStatus.OK) {
            throw new RuntimeException("PlayerBot is not registered");
        }

        return resp.getBody();
    }
}
