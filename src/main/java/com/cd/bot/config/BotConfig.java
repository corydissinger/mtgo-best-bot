package com.cd.bot.config;

import com.cd.bot.robot.RobotWrapper;
import com.cd.bot.tesseract.ImagePreProcessor;
import com.cd.bot.tesseract.TesseractWrapper;
import net.sourceforge.tess4j.TessAPI1;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.awt.*;

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
}