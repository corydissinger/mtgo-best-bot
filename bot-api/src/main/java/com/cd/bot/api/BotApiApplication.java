package com.cd.bot.api;

import com.cd.bot.api.controller.BotController;
import com.cd.bot.api.controller.BotStatusController;
import com.cd.bot.api.controller.BotCameraController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.multipart.MultipartResolver;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.servlet.MultipartConfigElement;

/**
 * Created by Cory on 5/15/2017.
 */
@SpringBootApplication
@EnableSwagger2
@EnableJpaRepositories
@ComponentScan(basePackageClasses = {BotController.class, BotStatusController.class, BotCameraController.class})
public class BotApiApplication {
    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(BotApiApplication.class, args);
    }

    @Autowired
    private Environment environment;

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

    @Bean
    public ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .description("Control the MTGO bots")
                .build();
    }

    @Bean
    public MultipartConfigElement multipartConfigElement() {
        int MAX_UPLOAD_SIZE = 10000000;

        MultipartConfigElement multipartConfigElement = new MultipartConfigElement(environment.getRequiredProperty("upload.temp.folder"),
                MAX_UPLOAD_SIZE, MAX_UPLOAD_SIZE * 2, MAX_UPLOAD_SIZE / 2);

        return multipartConfigElement;
    }

    @Bean
    public MultipartResolver multipartResolver() {
        org.springframework.web.multipart.commons.CommonsMultipartResolver multipartResolver = new org.springframework.web.multipart.commons.CommonsMultipartResolver();
        return multipartResolver;
    }

}
