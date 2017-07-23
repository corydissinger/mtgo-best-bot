package com.cd.bot.client.config;

import com.cd.bot.client.kafka.RobotLifecycleReceiver;
import com.cd.bot.client.robot.RobotMaster;
import com.cd.bot.client.robot.RobotWrapper;
import com.cd.bot.client.system.ProcessManager;
import com.cd.bot.client.tesseract.ImagePreProcessor;
import com.cd.bot.client.tesseract.RawLinesProcessor;
import com.cd.bot.client.tesseract.TesseractWrapper;
import com.cd.bot.model.domain.PlayerBot;
import com.cd.bot.model.domain.bot.LifecycleEvent;
import com.cd.bot.model.domain.bot.ProcessingLifecycleStatus;
import com.cd.bot.model.domain.repository.PlayerBotRepository;
import net.sourceforge.tess4j.TessAPI1;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.awt.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Created by Cory on 5/11/2017.
 */
@SpringBootApplication
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = {"com.cd.bot.model.domain"} )
@EntityScan(basePackages = {"com.cd.bot.model.domain"} )
@ComponentScan({ "com.cd.bot.client", "com.cd.bot.wrapper" })
@PropertySources({
    @PropertySource("classpath:client-application.properties"),
    @PropertySource("file:${app.home}/client-application.properties"), //wins
    @PropertySource("file:${app.home}/data.properties")
})
@EnableKafka
@EnableScheduling
@EnableAutoConfiguration(exclude={WebMvcAutoConfiguration.class})
public class BotConfig {

    private static final Logger log = LoggerFactory.getLogger(BotConfig.class);

    public static void main(String[] args) {
        ApplicationContext applicationContext = new SpringApplicationBuilder(BotConfig.class).headless(false).run(args);
    }

    @Autowired
    private Environment environment;

    @Autowired
    private ApplicationContext applicationContext;

    // ------------------- START KAFKA
    // ------------------- START KAFKA
    // ------------------- START KAFKA

    @Value("${kafka.bootstrap-servers}")
    public String bootstrapServers;

    @Bean
    public Map<String, Object> consumerConfigs() {
        Map<String, Object> props = new HashMap<>();
        // list of host:port pairs used for establishing the initial connections to the Kakfa cluster
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        // allows a pool of processes to divide the work of consuming and processing records
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "bot");

        return props;
    }

    @Bean
    public ConsumerFactory<String, LifecycleEvent> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerConfigs(), new StringDeserializer(), new JsonDeserializer<>(LifecycleEvent.class));
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, LifecycleEvent> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, LifecycleEvent> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());

        return factory;
    }

    @Bean
    public RobotLifecycleReceiver robotLifecycleReceiver() {
        return new RobotLifecycleReceiver();
    }

    // ------------------- END KAFKA
    // ------------------- END KAFKA
    // ------------------- END KAFKA    
    
    @Bean
    public Robot robot() {
        Robot theRobot = null;
        try {
            theRobot = new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
        }

        theRobot.setAutoWaitForIdle(true);

        return theRobot;
    }

    @Bean
    public Integer screenWidth() {
        return Integer.parseInt(environment.getRequiredProperty("screen.width"));
    }

    @Bean
    public Integer screenHeight() {
        return Integer.parseInt(environment.getRequiredProperty("screen.height"));
    }

    @Bean
    public RobotWrapper robotWrapper() {
        return new RobotWrapper();
    }

    @Bean
    public TessAPI1.TessBaseAPI tesseract() {
        TessAPI1.TessBaseAPI handle = TessAPI1.TessBaseAPICreate();
        TessAPI1.TessBaseAPIInit3(handle, environment.getRequiredProperty("tesseract.data.path"), "eng");
        TessAPI1.TessBaseAPISetVariable(handle, "user_words_suffix", environment.getRequiredProperty("tesseract.magic.cards.name.list"));

        return handle;
    }

    @Bean
    public ImagePreProcessor imagePreprocessor() {
        return new ImagePreProcessor();
    }

    @Bean
    public TesseractWrapper tesseractWrapper() {
        return new TesseractWrapper();
    }

    @Bean
    public Integer scalingFactor() {
        return Integer.parseInt(environment.getRequiredProperty("image.scaling.factor"));
    }

    @Bean
    public ProcessManager processMonitor() {
        return new ProcessManager();
    }

    @Bean
    public String executableName() {
        return environment.getRequiredProperty("executable.name");
    }

    @Bean
    public String executableDir() {
        return environment.getRequiredProperty("executable.dir");
    }

    @Bean
    public String botApiUrl() {
        return environment.getRequiredProperty("bot.model.url");
    }

    @Bean
    public String botName() {
        return environment.getRequiredProperty("bot.name");
    }

    @Bean
    public String password() {
        return environment.getRequiredProperty("password");
    }

    @Bean
    public Boolean executableShortcutOnly() {
        return Boolean.parseBoolean(environment.getProperty("executable.shortcut.only"));
    }

    @Bean
    public Integer executableXOffset() {
        return Integer.parseInt(environment.getProperty("executable.x.offset"));
    }

    @Bean
    public Integer executableYOffset() {
        return Integer.parseInt(environment.getProperty("executable.y.offset"));
    }

    @Bean
    public RawLinesProcessor rawLinesProcessor() {
        return new RawLinesProcessor();
    }

    @Bean
    public RobotMaster robotActorMaster() {
        return new RobotMaster();
    }

    @Autowired
    PlayerBotRepository playerBotRepository;

    @Bean
    public PlayerBot playerBot() {
        return playerBotRepository.findByName(botName());
    }

    @Bean
    public BlockingQueue<LifecycleEvent> processingEventsQueue() {
        BlockingQueue<LifecycleEvent> processingEventsQueue = new ArrayBlockingQueue<>(10);

        try {
            processingEventsQueue.put(new LifecycleEvent(ProcessingLifecycleStatus.APPLICATION_START, playerBot(), new Date()));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return processingEventsQueue;
    }

    //It's a 'transaction'
    @Scheduled(fixedDelay = 1)
    public void doWork() {
        LifecycleEvent nextEvent = processingEventsQueue().poll();

        if(nextEvent != null) {
            robotActorMaster().runBot(nextEvent);
        }
    }
}
