package com.cd.bot.wrapper;

import com.cd.bot.wrapper.http.BotCameraService;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.*;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableTransactionManagement
@ComponentScan({ "com.cd.bot.wrapper" })
@PropertySources({
        @PropertySource("classpath:api-wrapper-application.properties"),
        @PropertySource("file:${app.home}/api-wrapper-application.properties") //wins
})
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ApiWrapperConfig {

    @Bean
    @Qualifier("apiRestTemplate")
    public RestTemplate apiRestTemplate() {
        return new RestTemplate();
    }

    @Bean
    public HttpClient uploadHttpClient() {
        HttpClient uploadHttpClient = HttpClientBuilder.create().build();

        return uploadHttpClient;
    }

}