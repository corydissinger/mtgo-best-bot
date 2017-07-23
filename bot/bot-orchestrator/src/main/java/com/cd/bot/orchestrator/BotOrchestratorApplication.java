package com.cd.bot.orchestrator;

import com.cd.bot.model.domain.PlayerBot;
import com.cd.bot.model.domain.bot.LifecycleEvent;
import com.cd.bot.model.domain.repository.BotCameraRepository;
import com.cd.bot.model.domain.repository.LifecycleEventRepository;
import com.cd.bot.model.domain.repository.PlayerBotRepository;
import com.cd.bot.orchestrator.kafka.LifecycleEventSender;
import com.cd.bot.orchestrator.scheduling.ExecuteNextLifecycleEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.*;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.util.*;

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
@EnableKafka
@PropertySources({
        @PropertySource("classpath:orchestrator-application.properties"),
        @PropertySource("file:${app.home}/orchestrator-application.properties"), //wins
        @PropertySource("file:${app.home}/data.properties")
})
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
    BotCameraRepository botCameraRepository;

    @Autowired
    LifecycleEventRepository lifecycleEventRepository;

    @Autowired
    PlayerBotRepository playerBotRepository;

    // ------------------- START KAFKA
    // ------------------- START KAFKA
    // ------------------- START KAFKA

    @Value("${kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${kafka.topic.bot}")
    private String botTopic;

    @Bean
    public Map<String, Object> producerConfigs() {
        Map<String, Object> props = new HashMap<>();
        // list of host:port pairs used for establishing the initial connections to the Kakfa cluster
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        return props;
    }

    @Bean
    public ProducerFactory<String, Object> producerFactory() {
        DefaultKafkaProducerFactory<String, Object> factory = new DefaultKafkaProducerFactory<>(producerConfigs());
        factory.setValueSerializer(lifecycleEventSerializer());
        return factory;
    }

    @Bean
    public JsonSerializer lifecycleEventSerializer() {
        return new JsonSerializer<>(objectMapper());
    }

    @Bean
    public KafkaTemplate<String, Object> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    @Bean
    public LifecycleEventSender lifecycleEventSender() {
        return new LifecycleEventSender();
    }

    // ------------------- END KAFKA
    // ------------------- END KAFKA
    // ------------------- END KAFKA

    @Scheduled(fixedDelay = 5000)
    public void doWork() {
        List<PlayerBot> activeBots = playerBotRepository.findAll();

        for(PlayerBot playerBot : activeBots) {
            List<LifecycleEvent> lifecycleEvents = lifecycleEventRepository.findByPlayerBotAndLifecycleEventOutcomeIsNullAndAutomaticFalse(playerBot);

            for(LifecycleEvent lifecycleEvent : lifecycleEvents) {
                if(lifecycleEvent != null) {
                    ExecuteNextLifecycleEvent executeNextLifecycleEvent = executeNextLifecycleEvent(lifecycleEvent);
                    threadPoolTaskScheduler().execute(executeNextLifecycleEvent);
                }
            }
        }
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new HibernateAwareObjectMapper();
    }

    class HibernateAwareObjectMapper extends ObjectMapper {

        public HibernateAwareObjectMapper() {
            registerModule(new Hibernate5Module());
        }
    }

    @Bean
    public ThreadPoolTaskScheduler threadPoolTaskScheduler(){
        ThreadPoolTaskScheduler threadPoolTaskScheduler
                = new ThreadPoolTaskScheduler();
        threadPoolTaskScheduler.setPoolSize(5);
        threadPoolTaskScheduler.setThreadNamePrefix(
                "ThreadPoolTaskScheduler");
        return threadPoolTaskScheduler;
    }

    @Bean
    @Scope("prototype")
    public ExecuteNextLifecycleEvent executeNextLifecycleEvent(LifecycleEvent lifecycleEvent) {
        return new ExecuteNextLifecycleEvent(lifecycleEvent, lifecycleEventSender(), botTopic);
    }

    /* PREMATURE OPTIMIZATION - Delete query became busted after numerous refactors of JPA Entities
    @Scheduled(fixedRate = 60000)
    public void killOldCameraShots() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.HOUR, -1);
        Date oneHourBack = cal.getTime();
        botCameraRepository.deleteOlderThan(oneHourBack);
    }*/
}
