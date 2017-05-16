package com.cd.bot.controller;

import com.cd.bot.domain.Bot;
import com.cd.bot.domain.BotRepository;
import com.cd.bot.domain.BotStatus;
import com.cd.bot.domain.BotStatusRepository;
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

    @Autowired
    private BotRepository botRepository;

    @RequestMapping(value = "/bot/", method = RequestMethod.GET)
    private List<Bot> get() {
        List<Bot> bots = new ArrayList<>();
        botRepository.findAll().forEach(bots::add);
        return bots;
    }

    @RequestMapping(value = "/bot/{id}", method = RequestMethod.GET)
    private Bot get(@PathVariable final String id) {
        return botRepository.findOne(Long.parseLong(id));
    }

    @RequestMapping(value = "/bot", method = RequestMethod.POST)
    private ResponseEntity<Void> save(@RequestBody final Bot newBot) {
        botRepository.save(newBot);

        return ResponseEntity.ok(null);
    }
}
