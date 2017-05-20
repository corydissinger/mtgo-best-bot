package com.cd.bot.wrapper;

import com.cd.bot.wrapper.http.BotCameraService;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableTransactionManagement
@ComponentScan({ "com.cd.bot.wrapper" })
@PropertySource(value = "file:${app.home}/resources/application.properties")
public class WrapperConfig {

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