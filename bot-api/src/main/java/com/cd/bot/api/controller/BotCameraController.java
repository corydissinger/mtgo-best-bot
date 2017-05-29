package com.cd.bot.api.controller;

import com.cd.bot.api.BotApiApplication;
import com.cd.bot.model.domain.PlayerBot;
import com.cd.bot.model.domain.BotCamera;
import com.cd.bot.model.domain.repository.BotCameraRepository;
import com.cd.bot.model.domain.repository.PlayerBotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Cory on 5/15/2017.
 */
@Controller
public class BotCameraController {

    public static final String BOT_CAMERA_RECENT = "/botcamera/recent/{name}";
    public static final String ENDPOINT_ROINT = "/botcamera";

    @Autowired
    private BotCameraRepository botCameraRepository;

    @Autowired
    private PlayerBotRepository playerBotRepository;

    @RequestMapping(value = ENDPOINT_ROINT + "/{name}", method = RequestMethod.POST)
    @PreAuthorize(BotApiApplication.HAS_AUTH_ROLE_ORCHESTRATOR)
    public @ResponseBody Long uploadCamera(@RequestParam("file") MultipartFile file, @PathVariable("name") final String name) throws IOException {
        BotCamera botCam = new BotCamera(file.getBytes(), new Date());
        PlayerBot playerBot = playerBotRepository.findByName(name);

        botCam.setPlayerBot(playerBot);

        botCam = botCameraRepository.save(botCam);
        return botCam.getId();
    }

    @RequestMapping(value = ENDPOINT_ROINT + "/id/{id}", method = RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE)
    @PreAuthorize(BotApiApplication.HAS_AUTH_ROLE_ORCHESTRATOR)
    public @ResponseBody byte[] getCam(@PathVariable final Long id) {
        BotCamera botCam = botCameraRepository.findOne(id);

        return botCam.getScreenShot();
    }

    @RequestMapping(value = ENDPOINT_ROINT + "/name/{name}", method = RequestMethod.GET)
    @PreAuthorize(BotApiApplication.HAS_AUTH_ROLE_ORCHESTRATOR)
    public @ResponseBody List<Long> recent(@PathVariable final String name) {
        PlayerBot playerBot = playerBotRepository.findByName(name);

        List<Long> recent = botCameraRepository.findByPlayerBot(playerBot).stream().map(aBot -> aBot.getId()).collect(Collectors.toList());

        return recent;
    }
}
