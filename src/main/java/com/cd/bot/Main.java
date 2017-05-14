package com.cd.bot;

import com.cd.bot.akka.RobotActor;
import com.cd.bot.akka.RobotActorMaster;
import com.cd.bot.config.BotConfig;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {

    public static void main(String[] args) {
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(BotConfig.class);

        RobotActorMaster master = applicationContext.getBean(RobotActorMaster.class);

        master.runBot();
    }

}
