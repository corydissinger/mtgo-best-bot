package com.cd.bot;

import com.cd.bot.model.domain.repository.BotCameraRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Cory on 5/16/2017.
 */
@SpringBootApplication
@EnableJpaRepositories(basePackages = {"com.cd.bot.model.domain"} )
@EntityScan(basePackages = {"com.cd.bot.model.domain"} )
@EnableScheduling
@PropertySource(value = "classpath:/resources/orchestrator-application.properties")
public class BotOrchestratorApplication {
    public static void main(String[] args) {
        SpringApplication.run(BotOrchestratorApplication.class, args);
    }

    //TODO
    //TODO
    //TODO -  - - - Do not forget, the pre sell tumbler
    //TODO
    //TODO

    @Autowired
    private BotCameraRepository botCameraRepository;

    @Scheduled(fixedRate = 60000)
    public void killOldCameraShots() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.HOUR, -1);
        Date oneHourBack = cal.getTime();
        botCameraRepository.deleteOlderThan(oneHourBack);
    }
}
