package com.cd.bot.api.controller;

import com.cd.bot.api.BotApiApplication;
import com.cd.bot.model.domain.PlayerBot;
import com.cd.bot.model.domain.bot.LifecycleEvent;
import com.cd.bot.model.domain.bot.LifecycleEventOutcome;
import com.cd.bot.model.domain.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Cory on 5/16/2017.
 */
@RestController
@RequestMapping("/api")
public class BotController {

    public static final String ENDPOINT_ROOT = "/bot";

    @Autowired
    PlayerBotRepository playerBotRepository;

    @Autowired
    BotCameraRepository botCameraRepository;

    @Autowired
    OwnedTradeableCardRepository ownedTradeableCardRepository;

    @Autowired
    LifecycleEventRepository lifecycleEventRepository;

    @Autowired
    LifecycleEventOutcomeRepository lifecycleEventOutcomeRepository;

    @RequestMapping(value = ENDPOINT_ROOT, method = RequestMethod.GET)
    @PreAuthorize(BotApiApplication.HAS_AUTH_ROLE_ORCHESTRATOR)
    public List<PlayerBot> get() {
        List<PlayerBot> playerBots = new ArrayList<>();
        playerBotRepository.findAll().forEach(playerBots::add);
        return playerBots;
    }

    @RequestMapping(value = ENDPOINT_ROOT + "/{id}", method = RequestMethod.GET)
    @PreAuthorize(BotApiApplication.HAS_AUTH_ROLE_ORCHESTRATOR)
    public PlayerBot get(@PathVariable final String id) {
        return playerBotRepository.findOne(Long.parseLong(id));
    }

    @RequestMapping(value = ENDPOINT_ROOT + "/name/{name}", method = RequestMethod.GET)
    @PreAuthorize(BotApiApplication.HAS_AUTH_ROLE_ORCHESTRATOR)
    public PlayerBot getByName(@PathVariable final String name) {
        return playerBotRepository.findByName(name);
    }

    @RequestMapping(value = ENDPOINT_ROOT + "/details/{name}", method = RequestMethod.GET)
    @PreAuthorize(BotApiApplication.HAS_AUTH_ROLE_ORCHESTRATOR)
    public PlayerBot details(@PathVariable final String name) {
        PlayerBot playerBot = playerBotRepository.findByName(name);
        List<LifecycleEvent> mostRecentEvents = lifecycleEventRepository.findByPlayerBotAndLifecycleEventOutcomeIsNull(playerBot);

        playerBot.setBotCameras(botCameraRepository.findByPlayerBot(playerBot));
        playerBot.setBotCards(ownedTradeableCardRepository.findByPlayerBot(playerBot));
        playerBot.setStatus(getStatusFromEvent(mostRecentEvents));

        return playerBot;
    }

    @RequestMapping(value = ENDPOINT_ROOT, method = RequestMethod.POST)
    @PreAuthorize(BotApiApplication.HAS_AUTH_ROLE_ORCHESTRATOR)
    public ResponseEntity<Void> save(@RequestBody final PlayerBot newPlayerBot) {
        playerBotRepository.save(newPlayerBot);

        return ResponseEntity.ok(null);
    }

    private String getStatusFromEvent(List<LifecycleEvent> mostRecentEvents) {
        for(LifecycleEvent lifecycleEvent : mostRecentEvents) {
            if(lifecycleEvent.getLifecycleEventOutcome() == null) {
                return "Processing";
            }
        }

        return "Ready";
    }
}
