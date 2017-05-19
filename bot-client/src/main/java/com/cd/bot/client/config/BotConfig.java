package com.cd.bot.client.config;

import com.cd.bot.client.robot.RobotMaster;
import com.cd.bot.client.service.BotCameraService;
import com.cd.bot.client.robot.RobotWrapper;
import com.cd.bot.client.system.ProcessManager;
import com.cd.bot.client.tesseract.ImagePreProcessor;
import com.cd.bot.client.tesseract.RawLinesProcessor;
import com.cd.bot.client.tesseract.TesseractWrapper;
import net.sourceforge.tess4j.TessAPI1;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.client.RestTemplate;

import java.awt.*;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Cory on 5/11/2017.
 */
@Configuration
@EnableTransactionManagement
@ComponentScan({ "com.cd.bot.client" })
@PropertySource(value = "file:${app.home}/bot-client/resources/application.properties")
public class BotConfig {

    private static final Logger log = LoggerFactory.getLogger(BotConfig.class);

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
    public String botApiUrl() {
        return environment.getRequiredProperty("bot.api.url");
    }

    @Bean
    public String botName() {
        return environment.getRequiredProperty("bot.name");
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

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public RestTemplate cameraRestTemplate() {
        RestTemplate cameraRestTemplate = new RestTemplate();
        FormHttpMessageConverter formHttpMessageConverter = new FormHttpMessageConverter();
        formHttpMessageConverter.setCharset(Charset.forName("UTF8"));

        cameraRestTemplate.getMessageConverters().add( formHttpMessageConverter );
        cameraRestTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        cameraRestTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());

        final String debug = environment.getProperty("debug");

        if(Boolean.parseBoolean(debug)) {
            ClientHttpRequestInterceptor ri = loggingHttpInterceptor();
            List<ClientHttpRequestInterceptor> ris = new ArrayList<>();
            ris.add(ri);
            cameraRestTemplate.setInterceptors(ris);
        }

        return cameraRestTemplate;
    }

    @Bean
    public BotCameraService botCameraService() {
        return new BotCameraService();
    }

    @Bean
    public ClientHttpRequestInterceptor loggingHttpInterceptor() {
        //TODO - Replacing with lambda is a lie!
        ClientHttpRequestInterceptor loggingHttpInterceptor = new ClientHttpRequestInterceptor () {
            @Override
            public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {

                log.debug("Outbound request=" + request + System.lineSeparator() + "Outbound body=" + new String(body));
                
                ClientHttpResponse response = execution.execute(request, body);

                log.debug("Inbound response=" + response + System.lineSeparator() + "Inbound body=" + new String(IOUtils.toString(response.getBody())));

                return response;
            }
        };

        return loggingHttpInterceptor;
    }

}
