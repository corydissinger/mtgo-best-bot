package com.cd.bot.wrapper.http;

import com.cd.bot.api.controller.BotCameraController;
import com.cd.bot.api.controller.BotController;
import com.cd.bot.model.domain.PlayerBot;
import com.cd.bot.model.domain.BotCamera;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.impl.client.BasicResponseHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Arrays;

/**
 * Created by Cory on 5/16/2017.
 */
public class BotCameraService {

    private static final Logger log = LoggerFactory.getLogger(BotCameraService.class);

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private String botApiUrl;

    @Autowired
    private HttpClient uploadHttpClient;

    public Long saveBotCam(BotCamera botCamera) {
        HttpEntity entity = MultipartEntityBuilder
                .create()
                .addPart("file", new ByteArrayBody(botCamera.getScreenShot(), "botSnapshot"))
                .build();

        Long idResponse = -1L;

        try {
            HttpPost httpPost = new HttpPost(botApiUrl + BotCameraController.ENDPOINT_ROINT + "/" + botCamera.getPlayerBot().getName());
            httpPost.setEntity(entity);
            HttpResponse response = uploadHttpClient.execute(httpPost);
            ResponseHandler<String> handler = new BasicResponseHandler();

            if(response != null &&
                response.getStatusLine() != null &&
                response.getStatusLine().getStatusCode() == HttpStatus.OK.value()) {

                final String rawResult = handler.handleResponse(response);

                idResponse = Long.parseLong(rawResult);
            } else {
                log.error("Failed to upload pictar!");
                log.error("Headers received: " + Arrays.stream(response.getAllHeaders()).map(header -> new String(header.getName() + ": " + header.getValue()))
                                                                                        .reduce("", (result, next) -> System.lineSeparator() + next));
                log.error("Body received: " + System.lineSeparator() + handler.handleResponse(response));
            }
        } catch (IOException e) {
            log.error("Failed to upload pictar!");
            log.error(e.getMessage());
        }

        return idResponse;
    }

    public PlayerBot registerOrLoadSelf(PlayerBot playerBot) {
        ResponseEntity<PlayerBot> resp;

        try {
            resp = restTemplate.getForEntity(botApiUrl + BotController.ENDPOINT_ROOT + "/name/" + playerBot.getName(), PlayerBot.class);
        } catch (RestClientException e) {
            log.error("Failed to log in!");
            log.error(e.getMessage());
            throw new RuntimeException();
        }


        if(resp.getStatusCode() != HttpStatus.OK) {
            throw new RuntimeException("PlayerBot is not registered");
        }

        return resp.getBody();
    }
}
