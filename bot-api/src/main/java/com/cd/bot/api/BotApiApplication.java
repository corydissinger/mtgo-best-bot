package com.cd.bot.api;

import com.cd.bot.model.domain.repository.BotCameraRepository;
import com.cd.bot.model.domain.repository.OwnedTradeableCardRepository;
import com.cd.bot.model.domain.repository.PlayerBotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.web.WebMvcRegistrationsAdapter;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.*;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.lang.reflect.Method;

/**
 * Created by Cory on 5/15/2017.
 */
@SpringBootApplication
@EnableJpaRepositories(basePackages = {"com.cd.bot.model.domain"} )
@EntityScan(basePackages = {"com.cd.bot.model.domain"} )
@PropertySources({
        @PropertySource("classpath:api-application.properties"),
        @PropertySource("file:${app.home}/api-application.properties"), //wins
        @PropertySource("file:${app.home}/data.properties")
})
@Import({BotApiSecurityConfig.class})
public class BotApiApplication extends WebMvcConfigurerAdapter {
    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(BotApiApplication.class, args);
    }

    public static final String ROLE_ORCHESTRATOR = "ROLE_ORCHESTRATOR";
    public static final String HAS_AUTH_ROLE_ORCHESTRATOR = "hasAuthority('"+ROLE_ORCHESTRATOR+"')";

    @Autowired
    private Environment environment;

    @Bean
    public String botClientUrl() {
        return environment.getRequiredProperty("bot.client.url"); //Needs to be smarter
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        if (!registry.hasMappingForPattern("/app/**")) {
            registry.addResourceHandler("/app/**").addResourceLocations(
                    "classpath:/META-INF/resources/webjars/bot-ui/1.0.2/");
        }
        if (!registry.hasMappingForPattern("/js/**")) {
            registry.addResourceHandler("/js/**").addResourceLocations(
                    "classpath:/META-INF/resources/webjars/bot-ui/1.0.2/js/");
        }
    }

}
