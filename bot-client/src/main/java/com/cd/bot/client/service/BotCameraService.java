package com.cd.bot.client.service;

import com.cd.bot.api.controller.BotCameraController;
import com.cd.bot.api.controller.BotController;
import com.cd.bot.api.domain.Bot;
import com.cd.bot.api.domain.BotCamera;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
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
        HttpEntity<byte[]> byteEntity = new HttpEntity<>(botCamera.getScreenShot());
        ResponseEntity<Long> idResponse = restTemplate.postForEntity(botApiUrl + BotCameraController.CAMERA_ROOT_URL + botCamera.getBot().getName(), byteEntity, Long.class);

        return idResponse.getBody();
    }

    public Bot registerOrLoadSelf(Bot bot) {
        ResponseEntity<Bot> resp = restTemplate.getForEntity(botApiUrl + BotController.BOT_ROOT_URL + "/name/" + bot.getName(), Bot.class);

        if(resp.getStatusCode() != HttpStatus.OK) {
            throw new RuntimeException("Bot is not registered");
        }

        return resp.getBody();
    }
}
