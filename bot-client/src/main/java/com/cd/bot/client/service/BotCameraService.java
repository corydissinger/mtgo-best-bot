package com.cd.bot.client.service;

import com.cd.bot.api.controller.BotCameraController;
import com.cd.bot.api.controller.BotController;
import com.cd.bot.api.domain.Bot;
import com.cd.bot.api.domain.BotCamera;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

/**
 * Created by Cory on 5/16/2017.
 */
public class BotCameraService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private String botApiUrl;

    public Long saveBotCam(BotCamera botCamera) {
        ResponseEntity<Long> idResponse = restTemplate.postForEntity(botApiUrl + BotCameraController.CAMERA_ROOT_URL, botCamera, Long.class);

        return idResponse.getBody();
    }

    public Boolean registerOrLoadSelf(Bot bot) {
        ResponseEntity<Void> resp = restTemplate.postForEntity(botApiUrl + BotController.BOT_ROOT_URL, bot, Void.class);

        return resp.getStatusCode() == HttpStatus.CREATED || resp.getStatusCode() == HttpStatus.FOUND;
    }
}
