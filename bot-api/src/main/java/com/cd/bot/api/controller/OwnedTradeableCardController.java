package com.cd.bot.api.controller;

import com.cd.bot.model.domain.Card;
import com.cd.bot.model.domain.OwnedTradeableCard;
import com.cd.bot.model.domain.PlayerBot;
import com.cd.bot.model.domain.repository.CardRepository;
import com.cd.bot.model.domain.repository.OwnedTradeableCardRepository;
import com.cd.bot.model.domain.repository.PlayerBotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by Cory on 5/20/2017.
 */
@RestController
public class OwnedTradeableCardController {

    public static final String ENDPOINT_ROOT = "/ownedcards";

    @Autowired
    private PlayerBotRepository playerBotRepository;

    @Autowired
    private OwnedTradeableCardRepository ownedTradeableCardRepository;

    @Autowired
    private CardRepository cardRepository;

    @RequestMapping(value = ENDPOINT_ROOT + "/name/{name}", method = RequestMethod.GET)
    public List<OwnedTradeableCard> getCards(@PathVariable final String name) {
        PlayerBot playerBot = playerBotRepository.findByName(name);

        return ownedTradeableCardRepository.findByPlayerBot(playerBot);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(value = ENDPOINT_ROOT + "/name/{name}", method = RequestMethod.POST)
    public void postCards(@RequestBody List<OwnedTradeableCard> ownedTradeableCards, @PathVariable final String name) {
        PlayerBot playerBot = playerBotRepository.findByName(name);

        for(OwnedTradeableCard ownedTradeableCard : ownedTradeableCards) {
            Card card = ownedTradeableCard.getCard();

            Card exists = cardRepository.findByName(card.getName());

            if (exists == null) {
                ownedTradeableCard.setCard(cardRepository.save(card));
            } else {
                ownedTradeableCard.setCard(exists);
            }

            ownedTradeableCard.setPlayerBot(playerBot);
            ownedTradeableCardRepository.save(ownedTradeableCard);
        }
    }
}
