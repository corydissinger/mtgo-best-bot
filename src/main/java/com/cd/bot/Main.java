package com.cd.bot;

import akka.actor.ActorRef;
import akka.actor.Inbox;
import com.cd.bot.akka.RobotActor;
import com.cd.bot.config.BotConfig;
import com.cd.bot.robot.RobotWrapper;
import com.cd.bot.robot.exception.ApplicationDownException;
import com.cd.bot.tesseract.TesseractWrapper;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(BotConfig.class);

        TesseractWrapper tesseractWrapper = (TesseractWrapper) applicationContext.getBean("tesseractWrapper");
        RobotWrapper robotWrapper = (RobotWrapper) applicationContext.getBean("robotWrapper");
        ActorRef robotRef = (ActorRef) applicationContext.getBean("robotRef");
        Inbox inbox = (Inbox) applicationContext.getBean("inbox");

        RobotActor.InputStringEvent e = new RobotActor.InputStringEvent();

        e.setTextToInput("Nova4545");

        inbox.send(robotRef, e);

//        do {
//            BufferedImage bi = null;
//            try {
//                bi = robotWrapper.getCurrentScreen();
//            } catch (ApplicationDownException e) {
//                e.printStackTrace();
//                robotWrapper.reInit();
//            }
//
//            List<String> bleh = tesseractWrapper.getRawText(bi);
//
//            bleh.stream().forEach(aBleh -> System.out.println(aBleh));
//        } while(true);
    }

}
