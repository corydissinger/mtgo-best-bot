package com.cd.bot.api.controller;

import com.cd.bot.model.domain.BotStatus;
import com.cd.bot.model.domain.BotStatusRepository;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Cory on 5/15/2017.
 */
@RestController(value = "/botstatus")
public class BotStatusController {

    @Autowired
    private BotStatusRepository botStatusRepository;
}
