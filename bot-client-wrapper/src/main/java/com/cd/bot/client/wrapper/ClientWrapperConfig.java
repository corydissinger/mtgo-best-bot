package com.cd.bot.client.wrapper;

import com.cd.bot.wrapper.http.BotClientRestClient;
import org.springframework.context.annotation.*;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableTransactionManagement
@ComponentScan({ "com.cd.bot.client.wrapper" })
@PropertySources({
        @PropertySource("classpath:wrapper-application.properties"),
        @PropertySource("file:${app.home}/wrapper-application.properties") //wins
})
public class ClientWrapperConfig {

    @Bean
    public RestTemplate clientRestTemplate() {
        return new RestTemplate();
    }

    @Bean
    public BotClientRestClient botCameraService() {
        return new BotClientRestClient();
    }

}