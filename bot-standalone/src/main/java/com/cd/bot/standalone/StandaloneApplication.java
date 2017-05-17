package com.cd.bot.standalone;

import com.cd.bot.BotOrchestratorApplication;
import com.cd.bot.api.BotApiApplication;
import com.cd.bot.client.config.BotConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;

/**
 * Created by Cory on 5/16/2017.
 */
@SpringBootApplication
@ComponentScan(basePackageClasses = { BotApiApplication.class, BotOrchestratorApplication.class, BotConfig.class})
public class StandaloneApplication {
    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(StandaloneApplication.class, args);
    }
}
