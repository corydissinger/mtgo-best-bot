package com.cd.bot.client.service;

import com.cd.bot.api.controller.BotCameraController;
import com.cd.bot.api.controller.BotController;
import com.cd.bot.api.domain.Bot;
import com.cd.bot.api.domain.BotCamera;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
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
        MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
        ByteArrayResource contentsAsResource = new ByteArrayResource(botCamera.getScreenShot());
        map.add("file", contentsAsResource);
        HttpHeaders headers = new HttpHeaders();

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(map, headers);

        Long idResponse = -1l;

        try {
            idResponse = restTemplate.postForObject(botApiUrl + BotCameraController.CAMERA_ROOT_URL + botCamera.getBot().getName(), requestEntity, Long.class);
        } catch (RestClientException e) {
            e.printStackTrace();
        }

        return idResponse;
    }

    public Bot registerOrLoadSelf(Bot bot) {
        ResponseEntity<Bot> resp = restTemplate.getForEntity(botApiUrl + BotController.BOT_ROOT_URL + "/name/" + bot.getName(), Bot.class);

        if(resp.getStatusCode() != HttpStatus.OK) {
            throw new RuntimeException("Bot is not registered");
        }

        return resp.getBody();
    }
}
