package com.cd.bot.api.controller;

import com.cd.bot.model.domain.repository.BotStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Cory on 5/15/2017.
 */
@RestController(value = "/api/botstatus")
public class BotStatusController {

    @Autowired
    private BotStatusRepository botStatusRepository;
}
