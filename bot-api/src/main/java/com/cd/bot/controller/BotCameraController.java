package com.cd.bot.controller;

import com.cd.bot.domain.Bot;
import com.cd.bot.domain.BotCamera;
import com.cd.bot.domain.BotCameraRepository;
import com.cd.bot.domain.BotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.print.attribute.standard.Media;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Cory on 5/15/2017.
 */
@Controller
public class BotCameraController {

    @Autowired
    private BotCameraRepository botCameraRepository;

    @Autowired
    private BotRepository botRepository;

    @RequestMapping(value = "/botcamera/{name}", method = RequestMethod.POST)
    public @ResponseBody Long uploadCamera(@RequestPart MultipartFile file, @PathVariable final String name) throws IOException {
        BotCamera botCam = new BotCamera(file.getBytes(), new Date());
        Bot bot = botRepository.findByName(name);

        botCam.setBot(bot);

        botCam = botCameraRepository.save(botCam);
        return botCam.getId();
    }

    @ResponseBody
    @RequestMapping(value = "/botcamera/{id}", method = RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] getCam(@PathVariable final String id) {
        BotCamera botCam = botCameraRepository.findOne(Long.parseLong(id));

        return botCam.getScreenShot();
    }

    @ResponseBody
    @RequestMapping(value = "/botcamera/recent/{name}", method = RequestMethod.GET)
    public List<Long> recent(@PathVariable final String name) {
        Bot bot = botRepository.findByName(name);

        List<Long> recent = botCameraRepository.findByBot(bot).stream().map(aBot -> aBot.getId()).collect(Collectors.toList());

        return recent;
    }
}
