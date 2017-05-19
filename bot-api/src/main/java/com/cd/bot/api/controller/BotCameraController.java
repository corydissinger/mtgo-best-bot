package com.cd.bot.api.controller;

import com.cd.bot.api.domain.Bot;
import com.cd.bot.api.domain.BotCamera;
import com.cd.bot.api.domain.BotCameraRepository;
import com.cd.bot.api.domain.BotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
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
    public static final String CAMERA_ROOT_URL = "/botcamera";

    @Autowired
    private BotCameraRepository botCameraRepository;

    @Autowired
    private BotRepository botRepository;

    @RequestMapping(value = CAMERA_ROOT_URL + "/{name}", method = RequestMethod.POST)
    public @ResponseBody Long uploadCamera(@RequestParam("file") MultipartFile file, @PathVariable("name") final String name) throws IOException {
        BotCamera botCam = new BotCamera(file.getBytes(), new Date());
        Bot bot = botRepository.findByName(name);

        botCam.setBot(bot);

        botCam = botCameraRepository.save(botCam);
        return botCam.getId();
    }

    @ResponseBody
    @RequestMapping(value = CAMERA_ROOT_URL + "/id/{id}", method = RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] getCam(@PathVariable final Long id) {
        BotCamera botCam = botCameraRepository.findOne(id);

        return botCam.getScreenShot();
    }

    @ResponseBody
    @RequestMapping(value = CAMERA_ROOT_URL + "/name/{name}", method = RequestMethod.GET)
    public List<Long> recent(@PathVariable final String name) {
        Bot bot = botRepository.findByName(name);

        List<Long> recent = botCameraRepository.findByBot(bot).stream().map(aBot -> aBot.getId()).collect(Collectors.toList());

        return recent;
    }
}
