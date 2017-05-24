package com.cd.bot.client.config;

import com.cd.bot.client.robot.RobotMaster;
import com.cd.bot.client.robot.RobotWrapper;
import com.cd.bot.client.system.ProcessManager;
import com.cd.bot.client.tesseract.ImagePreProcessor;
import com.cd.bot.client.tesseract.RawLinesProcessor;
import com.cd.bot.client.tesseract.TesseractWrapper;
import net.sourceforge.tess4j.TessAPI1;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.awt.*;

/**
 * Created by Cory on 5/11/2017.
 */
@Configuration
@EnableTransactionManagement
@ComponentScan({ "com.cd.bot.client", "com.cd.bot.wrapper" })
@PropertySources({
    @PropertySource("classpath:client-application.properties"),
    @PropertySource("file:${app.home}/client-application.properties") //wins
})
@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class,HibernateJpaAutoConfiguration.class})
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class BotConfig extends WebSecurityConfigurerAdapter {

    private static final Logger log = LoggerFactory.getLogger(BotConfig.class);

    public static final String ROLE_MASTER = "ROLE_MASTER";

    public static void main(String[] args) {
        ApplicationContext applicationContext = new SpringApplicationBuilder(BotConfig.class).headless(false).run(args);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().anyRequest().authenticated()
                .and()
                .x509()
                .subjectPrincipalRegex("CN=(.*?)(?:,|$)")
                .userDetailsService(userDetailsService());
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) {
                if (username.equals("master")) {
                    return new User(username, "",
                            AuthorityUtils
                                    .commaSeparatedStringToAuthorityList(ROLE_MASTER));
                }
                return null;
            }
        };
    }

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

}
