package com.cd.bot.client.wrapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.converter.StringMessageConverter;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

@Configuration
@ComponentScan({ "com.cd.bot.client.wrapper" })
@PropertySources({
        @PropertySource("classpath:client-wrapper-application.properties"),
        @PropertySource("file:${app.home}/client-wrapper-application.properties") //wins
})
public class ClientWrapperConfig {

    @Autowired
    private Environment environment;

    @Bean
    public WebSocketStompClient webSocketStompClient() {
        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.afterPropertiesSet();
        WebSocketClient webSocketClient = new StandardWebSocketClient();
        WebSocketStompClient stompClient = new WebSocketStompClient(webSocketClient);
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());
        stompClient.setTaskScheduler(taskScheduler); // for heartbeats
        return stompClient;
    }

    @Bean
    public String botClientUrl() {
        return environment.getRequiredProperty("bot.client.url");
    }
}