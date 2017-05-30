package com.cd.bot;

import com.cd.bot.client.model.AssumedScreenTest;
import com.cd.bot.client.model.LifecycleEvent;
import com.cd.bot.client.model.LifecycleEventOutcome;
import com.cd.bot.client.model.ProcessingLifecycleStatus;
import com.cd.bot.model.domain.repository.BotCameraRepository;
import com.cd.bot.wrapper.ApiWrapperConfig;
import com.cd.bot.wrapper.http.BotPushService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.*;

/**
 * Created by Cory on 5/16/2017.
 */
@SpringBootApplication
@EnableJpaRepositories(basePackages = {"com.cd.bot.model.domain"} )
@EntityScan(basePackages = {"com.cd.bot.model.domain"} )
@EnableScheduling
@EnableAsync
@EnableAutoConfiguration(exclude={WebMvcAutoConfiguration.class})
@PropertySources({
        @PropertySource("classpath:orchestrator-application.properties"),
        @PropertySource("file:${app.home}/orchestrator-application.properties") //wins
})
@Import({ApiWrapperConfig.class})
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
    private BotPushService botPushService;

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

    @Scheduled(fixedDelay = 160)
    public void doWork() {
        final ConcurrentLinkedQueue<LifecycleEvent> eventQueue = eventQueue();

        if(eventQueue.isEmpty()) {
            eventQueue.add(new LifecycleEvent(AssumedScreenTest.NOT_NEEDED, ProcessingLifecycleStatus.APPLICATION_START));
        }

        Future<LifecycleEventOutcome> outcome = pushEvent(eventQueue.remove());

        try {
            final LifecycleEventOutcome finalOutcome = outcome.get();
            final ProcessingLifecycleStatus outcomeStatus = finalOutcome.getProcessingLifecycleStatus();

            switch(outcomeStatus) {
                case UNKNOWN:
                    eventQueue.add(new LifecycleEvent(AssumedScreenTest.LOGIN, ProcessingLifecycleStatus.LOGIN_READY));
                    break;
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    @Async
    public Future<LifecycleEventOutcome> pushEvent(LifecycleEvent lifecycleEvent) {
        return CompletableFuture.completedFuture(botPushService.pushEvent(lifecycleEvent, "baronvonfonz"));
    }
}
