package com.cd.bot.wrapper.http;

import com.cd.bot.api.controller.BotPushController;
import com.cd.bot.api.controller.OwnedTradeableCardController;
import com.cd.bot.client.model.LifecycleEvent;
import com.cd.bot.client.model.LifecycleEventOutcome;
import com.cd.bot.model.domain.OwnedTradeableCard;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.HttpsURLConnection;
import java.util.List;

/**
 * Created by Cory on 5/29/2017.
 */
@Service
public class BotPushService {

    private static final Logger log = LoggerFactory.getLogger(OwnedTradeableCardService.class);

    @Autowired
    @Qualifier("apiRestTemplate")
    private RestTemplate restTemplate;

    @Autowired
    private String apiUrl;

    public LifecycleEventOutcome pushEvent(final LifecycleEvent lifecycleEvent, String accountName) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Object> requestEntity = new HttpEntity<>(lifecycleEvent, headers);
        LifecycleEventOutcome outcome = null;

        try {
            restTemplate.exchange(apiUrl + BotPushController.ENDPOINT_ROOT + "/name/" + accountName, HttpMethod.POST, requestEntity, LifecycleEventOutcome.class);
        } catch (RestClientException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }

        return outcome;
    }


}
