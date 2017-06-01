package com.cd.bot.orchestrator;

import com.cd.bot.client.model.AssumedScreenTest;
import com.cd.bot.client.model.LifecycleEvent;
import com.cd.bot.client.model.LifecycleEventOutcome;
import com.cd.bot.client.model.ProcessingLifecycleStatus;
import com.cd.bot.client.wrapper.ClientWrapperConfig;
import com.cd.bot.model.domain.repository.BotCameraRepository;
import com.cd.bot.orchestrator.service.BotClientService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.*;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.*;
import java.util.function.Consumer;

/**
 * Created by Cory on 5/16/2017.
 */
@SpringBootApplication
@EnableJpaRepositories(basePackages = {"com.cd.bot.model.domain"} )
@EntityScan(basePackages = {"com.cd.bot.model.domain"} )
@ComponentScan(basePackages = {"com.cd.bot.client.wrapper", "com.cd.bot.orchestrator"})
@EnableScheduling
@EnableAsync
@EnableAutoConfiguration(exclude={WebMvcAutoConfiguration.class})
@PropertySources({
        @PropertySource("classpath:orchestrator-application.properties"),
        @PropertySource("file:${app.home}/orchestrator-application.properties") //wins
})
@Import(ClientWrapperConfig.class)
public class BotOrchestratorApplication {

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(BotOrchestratorApplication.class);

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(BotOrchestratorApplication.class);
        app.setWebEnvironment(false);
        ConfigurableApplicationContext ctx = app.run(args);

    }

    //TODO
    //TODO
    //TODO -  - - - Do not forget, the pre sell tumbler
    //TODO
    //TODO
    @Autowired
    private BotClientService botClientService;

    @Autowired
    private BotCameraRepository botCameraRepository;

    @Bean
    public ConcurrentLinkedQueue<LifecycleEvent> eventQueue() {
        return new ConcurrentLinkedQueue<>();
    }

    @Scheduled(fixedRate = 60000)
    public void killOldCameraShots() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.HOUR, -1);
        Date oneHourBack = cal.getTime();
        botCameraRepository.deleteOlderThan(oneHourBack);
    }

    @Scheduled(fixedDelay = 5000)
    public void doWork() {
        final ConcurrentLinkedQueue<LifecycleEvent> eventQueue = eventQueue();

        if(eventQueue.isEmpty()) {
            eventQueue.add(new LifecycleEvent(AssumedScreenTest.NOT_NEEDED, ProcessingLifecycleStatus.APPLICATION_START));
        }

        Consumer<LifecycleEventOutcome> outcomeConsumer = outcome -> {
            final ProcessingLifecycleStatus outcomeStatus = outcome.getProcessingLifecycleStatus();

            switch (outcomeStatus) {
                case UNKNOWN:
                    eventQueue.add(new LifecycleEvent(AssumedScreenTest.LOGIN, ProcessingLifecycleStatus.LOGIN_READY));
                    break;
            }
        };

        botClientService.pushEvent(eventQueue.remove(), outcomeConsumer);
    }

}
