package com.cd.bot.api.controller;

import com.cd.bot.api.BotApiApplication;
import com.cd.bot.api.service.ExecutedTradeService;
import com.cd.bot.model.domain.trade.ExecutedTrade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Cory on 5/22/2017.
 */
@RestController
public class ExecutedTradeController {

    public static final String ENDPOINT_ROOT = "/trades";

    @Autowired
    private ExecutedTradeService executedTradeService;

    @RequestMapping(value = ENDPOINT_ROOT, method = RequestMethod.POST)
    @PreAuthorize(BotApiApplication.HAS_AUTH_ROLE_ORCHESTRATOR)
    public Long create(@RequestBody ExecutedTrade executedTrade) {
        return executedTradeService.recordExecutedTrade(executedTrade);
    }

}
