package com.cd.bot.wrapper;

import com.cd.bot.wrapper.http.BotCameraService;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.context.annotation.*;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableTransactionManagement
@ComponentScan({ "com.cd.bot.wrapper" })
@PropertySources({
        @PropertySource("classpath:wrapper-application.properties"),
        @PropertySource("file:${app.home}/wrapper-application.properties") //wins
})
public class ApiWrapperConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public BotCameraService botCameraService() {
        return new BotCameraService();
    }

    @Bean
    public HttpClient uploadHttpClient() {
        HttpClient uploadHttpClient = HttpClientBuilder.create().build();

        return uploadHttpClient;
    }

}