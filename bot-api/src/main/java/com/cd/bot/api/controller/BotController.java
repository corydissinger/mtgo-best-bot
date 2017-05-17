package com.cd.bot.api.controller;

import com.cd.bot.api.domain.Bot;
import com.cd.bot.api.domain.BotRepository;
import com.cd.bot.api.domain.BotStatus;
import com.cd.bot.api.domain.BotStatusRepository;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Cory on 5/16/2017.
 */
@RestController
public class BotController {

    public static final String BOT_ROOT_URL = "/bot";

    @Autowired
    private BotRepository botRepository;

    @RequestMapping(value = BOT_ROOT_URL, method = RequestMethod.GET)
    private List<Bot> get() {
        List<Bot> bots = new ArrayList<>();
        botRepository.findAll().forEach(bots::add);
        return bots;
    }

    @RequestMapping(value = BOT_ROOT_URL + "/{id}", method = RequestMethod.GET)
    private Bot get(@PathVariable final String id) {
        return botRepository.findOne(Long.parseLong(id));
    }

    @RequestMapping(value = BOT_ROOT_URL, method = RequestMethod.POST)
    private ResponseEntity<Void> save(@RequestBody final Bot newBot) {
        botRepository.save(newBot);

        return ResponseEntity.ok(null);
    }
}
