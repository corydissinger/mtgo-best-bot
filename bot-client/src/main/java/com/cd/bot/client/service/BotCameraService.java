package com.cd.bot.client.service;

import com.cd.bot.api.controller.BotCameraController;
import com.cd.bot.api.controller.BotController;
import com.cd.bot.api.domain.Bot;
import com.cd.bot.api.domain.BotCamera;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;

/**
 * Created by Cory on 5/16/2017.
 */
public class BotCameraService {

    private static final Logger log = LoggerFactory.getLogger(BotCameraService.class);

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private RestTemplate cameraRestTemplate;

    @Autowired
    private String botApiUrl;

    public Long saveBotCam(BotCamera botCamera) {
        MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
        ByteArrayResource contentsAsResource = new ByteArrayResource(botCamera.getScreenShot());
        map.add("file", contentsAsResource);

        HttpHeaders imageHeaders = new HttpHeaders();
        imageHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);

        HttpEntity<MultiValueMap<String, Object>> imageEntity = new HttpEntity<MultiValueMap<String, Object>>(map, imageHeaders);

        Long idResponse = -1l;

        try {
            ResponseEntity<Long> rawResp = cameraRestTemplate.exchange(botApiUrl + BotCameraController.CAMERA_ROOT_URL + botCamera.getBot().getName(), HttpMethod.POST, imageEntity, Long.class);
            idResponse = rawResp.getBody();
        } catch (RestClientException e) {
            log.error("Failed to upload pictar!");
            log.error(e.getMessage());
        }

        return idResponse;
    }

    public Bot registerOrLoadSelf(Bot bot) {
        ResponseEntity<Bot> resp;

        try {
            resp = restTemplate.getForEntity(botApiUrl + BotController.BOT_ROOT_URL + "/name/" + bot.getName(), Bot.class);
        } catch (RestClientException e) {
            log.error("Failed to log in!");
            log.error(e.getMessage());
            throw new RuntimeException();
        }


        if(resp.getStatusCode() != HttpStatus.OK) {
            throw new RuntimeException("Bot is not registered");
        }

        return resp.getBody();
    }
}
