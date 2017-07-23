package com.cd.bot.wrapper.http;

import com.cd.bot.api.controller.OwnedTradeableCardController;
import com.cd.bot.model.domain.trade.OwnedTradeableCard;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * Created by Cory on 5/20/2017.
 */
@Service
public class OwnedTradeableCardService {

    private static final Logger log = LoggerFactory.getLogger(OwnedTradeableCardService.class);

    @Autowired
    @Qualifier("apiRestTemplate")
    private RestTemplate restTemplate;

    @Autowired
    private String apiUrl;

    public void addCards(List<OwnedTradeableCard> ownedTradeableCards, String accountName) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Object> requestEntity = new HttpEntity<>(ownedTradeableCards, headers);

        try {
            restTemplate.exchange(apiUrl + OwnedTradeableCardController.ENDPOINT_ROOT + "/name/" + accountName, HttpMethod.POST, requestEntity, Void.class);
        } catch (RestClientException e) {
            log.error("Failed to add cards!");
            log.error(e.getMessage());
            throw new RuntimeException();
        }
    }

}
