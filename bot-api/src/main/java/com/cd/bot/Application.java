package com.cd.bot;

import com.cd.bot.controller.BotController;
import com.cd.bot.controller.BotStatusController;
import com.cd.bot.controller.BotCameraController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import org.springframework.context.annotation.ComponentScan;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Created by Cory on 5/15/2017.
 */
@SpringBootApplication
@EnableSwagger2
@ComponentScan(basePackageClasses = {BotController.class, BotStatusController.class, BotCameraController.class})
public class Application {
    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(Application.class, args);
    }

    @Bean
    public Docket botApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("bot-api")
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .description("Control the MTGO bots")
                .build();
    }

}
