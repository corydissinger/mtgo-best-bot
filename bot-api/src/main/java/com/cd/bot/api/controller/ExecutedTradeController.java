package com.cd.bot.api.controller;

import com.cd.bot.api.service.ExecutedTradeService;
import com.cd.bot.model.domain.ExecutedTrade;
import com.cd.bot.model.domain.NpcTradeableCard;
import com.cd.bot.model.domain.OwnedTradeableCard;
import com.cd.bot.model.domain.SellOrder;
import com.cd.bot.model.domain.repository.ExecutedTradeRepository;
import com.cd.bot.model.domain.repository.NpcBotRepository;
import com.cd.bot.model.domain.repository.OwnedTradeableCardRepository;
import com.cd.bot.model.domain.repository.PlayerBotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Cory on 5/22/2017.
 */
@RestController
public class ExecutedTradeController {

    public static final String ENDPOINT_ROOT = "/trades";

    @Autowired
    private ExecutedTradeService executedTradeService;

    @RequestMapping(value = ENDPOINT_ROOT, method = RequestMethod.POST)
    public Long create(@RequestBody ExecutedTrade executedTrade) {
        return executedTradeService.recordExecutedTrade(executedTrade);
    }

}
