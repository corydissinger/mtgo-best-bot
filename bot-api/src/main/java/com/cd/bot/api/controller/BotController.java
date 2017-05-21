package com.cd.bot.api.controller;

import com.cd.bot.model.domain.PlayerBot;
import com.cd.bot.model.domain.repository.PlayerBotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Cory on 5/16/2017.
 */
@RestController
public class BotController {

    public static final String ENDPOINT_ROOT = "/bot";

    @Autowired
    private PlayerBotRepository playerBotRepository;

    @RequestMapping(value = ENDPOINT_ROOT, method = RequestMethod.GET)
    private List<PlayerBot> get() {
        List<PlayerBot> playerBots = new ArrayList<>();
        playerBotRepository.findAll().forEach(playerBots::add);
        return playerBots;
    }

    @RequestMapping(value = ENDPOINT_ROOT + "/{id}", method = RequestMethod.GET)
    private PlayerBot get(@PathVariable final String id) {
        return playerBotRepository.findOne(Long.parseLong(id));
    }

    @RequestMapping(value = ENDPOINT_ROOT + "/name/{name}", method = RequestMethod.GET)
    private PlayerBot getByName(@PathVariable final String name) {
        return playerBotRepository.findByName(name);
    }

    @RequestMapping(value = ENDPOINT_ROOT, method = RequestMethod.POST)
    private ResponseEntity<Void> save(@RequestBody final PlayerBot newPlayerBot) {
        playerBotRepository.save(newPlayerBot);

        return ResponseEntity.ok(null);
    }
}
