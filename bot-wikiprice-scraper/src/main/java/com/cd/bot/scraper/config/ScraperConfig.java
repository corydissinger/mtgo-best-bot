package com.cd.bot.scraper.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.core.env.Environment;


/**
 * Created by Cory on 5/11/2017.
 */
@Configuration
@ComponentScan({ "com.cd.bot.scraper" })
@PropertySources({
    @PropertySource("classpath:scraper-application.properties"),
    @PropertySource("file:${app.home}/scraper-application.properties") //wins
})
@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class, WebMvcAutoConfiguration.class })
public class ScraperConfig {

    public static void main(String[] args) {
        ApplicationContext applicationContext = new SpringApplicationBuilder(ScraperConfig.class).headless(false).run(args);
        System.out.println("---------------------------");
        System.out.println("Hello world!");
        System.out.println("---------------------------");
    }

    @Autowired
    private Environment environment;

    @Autowired
    private ApplicationContext applicationContext;

}
