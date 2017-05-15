package com.cd.bot.controller;

import com.cd.bot.domain.BotStatus;
import com.cd.bot.domain.BotStatusRepository;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Cory on 5/15/2017.
 */
@RestController
public class BotStatusController {

    @Autowired
    private BotStatusRepository botStatusRepository;

    @ApiOperation(value = "Get a bot's status")
    @RequestMapping(value = "/{name}", method = RequestMethod.GET)
    public BotStatus get(@PathVariable final String name) {
        return botStatusRepository.findOne(name);
    }
}
