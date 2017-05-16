package com.cd.bot.controller;

import com.cd.bot.domain.BotCamera;
import com.cd.bot.domain.BotCameraRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.print.attribute.standard.Media;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

/**
 * Created by Cory on 5/15/2017.
 */
@Controller
public class BotCameraController {

    @Autowired
    private BotCameraRepository botCameraRepository;

    @RequestMapping(value = "/botcamera", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE }, method = RequestMethod.POST)
    public @ResponseBody BotCamera uploadCamera(@RequestPart MultipartFile file) throws IOException {
        BotCamera botCam = new BotCamera(file.getBytes(), new Date());

        botCameraRepository.save(botCam);
        return botCam;
    }

    @ResponseBody
    @RequestMapping(value = "/botcamera/{id}", method = RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] getCam(@PathVariable final String id) {
        BotCamera botCam = botCameraRepository.findOne(Long.parseLong(id));

        return botCam.getScreenShot();
    }
}
