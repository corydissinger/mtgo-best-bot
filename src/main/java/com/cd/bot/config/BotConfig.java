package com.cd.bot.config;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Inbox;
import akka.actor.Props;
import com.cd.bot.akka.RobotActor;
import com.cd.bot.akka.RobotActorMaster;
import com.cd.bot.robot.RobotWrapper;
import com.cd.bot.system.ProcessManager;
import com.cd.bot.tesseract.ImagePreProcessor;
import com.cd.bot.tesseract.RawLinesProcessor;
import com.cd.bot.tesseract.TesseractWrapper;
import net.sourceforge.tess4j.TessAPI1;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.awt.*;

import static com.cd.bot.akka.SpringExtension.SPRING_EXTENSION_PROVIDER;

/**
 * Created by Cory on 5/11/2017.
 */
@Configuration
@EnableTransactionManagement
@ComponentScan({ "com.cd.bot" })
@PropertySources({
        @PropertySource(value = { "classpath:application.properties" }),
        @PropertySource(value = "file:./application.properties", ignoreResourceNotFound = true )
})
public class BotConfig {

    @Autowired
    private Environment environment;

    @Autowired
    private ApplicationContext applicationContext;

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
    public String robotActorConst() {
        return "robotActor";
    }

    @Bean
    public ActorSystem actorSystem() {
        ActorSystem system = ActorSystem.create("LocalBotSystem");

        SPRING_EXTENSION_PROVIDER.get(system).initialize(applicationContext);

        return system;
    }

    @Bean
    public ActorRef robotRef() {
        return actorSystem().actorOf(SPRING_EXTENSION_PROVIDER.get(actorSystem()).props(robotActorConst()), robotActorConst());
    }

    @Bean
    public Inbox inbox() {
        return Inbox.create(actorSystem());
    }

    @Bean
    public RawLinesProcessor rawLinesProcessor() {
        return new RawLinesProcessor();
    }

    @Bean
    public RobotActorMaster robotActorMaster() {
        return new RobotActorMaster();
    }

}
