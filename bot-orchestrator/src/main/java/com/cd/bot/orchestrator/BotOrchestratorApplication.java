package com.cd.bot.orchestrator;

import com.cd.bot.client.wrapper.ClientWrapperConfig;
import com.cd.bot.model.domain.bot.LifecycleEvent;
import com.cd.bot.model.domain.repository.BotCameraRepository;
import com.cd.bot.model.domain.repository.LifecycleEventRepository;
import com.cd.bot.model.domain.repository.PlayerBotRepository;
import com.cd.bot.orchestrator.kafka.LifecycleEventSender;
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
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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
public class BotOrchestratorApplication {

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(BotOrchestratorApplication.class);
    private static final String BOT_TOPIC = "bot"; //kektus

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

    @Value("${kafka.bootstrap-servers}")
    private String bootstrapServers;

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

    @Scheduled(fixedDelay = 5000)
    public void doWork() {
        LifecycleEvent event = lifecycleEventRepository.findByOrderByTimeExecutedDesc();
        event.getPlayerBot();

        lifecycleEventSender().send(BOT_TOPIC, event);
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

    @Scheduled(fixedRate = 60000)
    public void killOldCameraShots() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.HOUR, -1);
        Date oneHourBack = cal.getTime();
        botCameraRepository.deleteOlderThan(oneHourBack);
        lifecycleEventRepository.deleteOlderThan(oneHourBack);
    }
}
