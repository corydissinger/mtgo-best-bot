package com.cd.bot.client;

import com.cd.bot.client.akka.RobotActor;
import com.cd.bot.client.akka.RobotActorMaster;
import com.cd.bot.client.config.BotConfig;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {

    public static void main(String[] args) {
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(BotConfig.class);

        RobotActorMaster master = applicationContext.getBean(RobotActorMaster.class);

        master.runBot();
    }

}
