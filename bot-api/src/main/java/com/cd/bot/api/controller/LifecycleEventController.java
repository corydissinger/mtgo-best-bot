package com.cd.bot.api.controller;

import com.cd.bot.api.BotApiApplication;
import com.cd.bot.model.domain.bot.AssumedScreenTest;
import com.cd.bot.model.domain.bot.LifecycleEvent;
import com.cd.bot.model.domain.bot.ProcessingLifecycleStatus;
import com.cd.bot.model.domain.repository.LifecycleEventRepository;
import com.cd.bot.model.domain.repository.PlayerBotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Cory on 6/2/2017.
 */
@RestController
@RequestMapping("/api")
public class LifecycleEventController {

    public static final String ENDPOINT_ROOT = "/event";

    @Autowired
    LifecycleEventRepository lifecycleEventRepository;

    @Autowired
    PlayerBotRepository playerBotRepository;

    @RequestMapping(value = ENDPOINT_ROOT + "/screen-test", method = RequestMethod.GET)
    @PreAuthorize(BotApiApplication.HAS_AUTH_ROLE_ORCHESTRATOR)
    public AssumedScreenTest[] getScreenTestEnums() {
        return AssumedScreenTest.values();
    }

    @RequestMapping(value = ENDPOINT_ROOT + "/lifecycle-status", method = RequestMethod.GET)
    @PreAuthorize(BotApiApplication.HAS_AUTH_ROLE_ORCHESTRATOR)
    public ProcessingLifecycleStatus[] getLifecycleStatus() {
        return ProcessingLifecycleStatus.values();
    }

    @RequestMapping(value = ENDPOINT_ROOT, method = RequestMethod.GET)
    @PreAuthorize(BotApiApplication.HAS_AUTH_ROLE_ORCHESTRATOR)
    public List<LifecycleEvent> get() {
        List<LifecycleEvent> lifecycleEvents = new ArrayList<>();
        lifecycleEventRepository.findAll().forEach(lifecycleEvents::add);
        return lifecycleEvents;
    }

    @RequestMapping(value = ENDPOINT_ROOT + "/{id}", method = RequestMethod.GET)
    @PreAuthorize(BotApiApplication.HAS_AUTH_ROLE_ORCHESTRATOR)
    public LifecycleEvent get(@PathVariable final String id) {
        return lifecycleEventRepository.findOne(Long.parseLong(id));
    }

    @RequestMapping(value = ENDPOINT_ROOT, method = RequestMethod.POST)
    @PreAuthorize(BotApiApplication.HAS_AUTH_ROLE_ORCHESTRATOR)
    public ResponseEntity<Void> save(@RequestBody final LifecycleEvent newLifecycleEvent) {
        newLifecycleEvent.setPlayerBot(playerBotRepository.findByName(newLifecycleEvent.getPlayerBot().getName()));
        lifecycleEventRepository.save(newLifecycleEvent);

        return ResponseEntity.ok(null);
    }    
}
