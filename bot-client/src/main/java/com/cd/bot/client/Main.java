package com.cd.bot.client;

import com.cd.bot.client.robot.RobotMaster;
import com.cd.bot.client.config.BotConfig;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {

    public static void main(String[] args) {
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(BotConfig.class);

        RobotMaster master = applicationContext.getBean(RobotMaster.class);

        master.runBot();
    }

}
