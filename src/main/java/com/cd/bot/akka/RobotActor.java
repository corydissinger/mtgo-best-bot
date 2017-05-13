package com.cd.bot.akka;

import akka.actor.AbstractActor;
import akka.actor.Props;
import com.cd.bot.robot.RobotWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Created by Cory on 5/13/2017.
 */
@Component
@Scope("prototype")
public class RobotActor extends AbstractActor {

    static public class MouseClickEvent {
        private int xOffset;
        private int yOffset;

        public int getxOffset() {
            return xOffset;
        }

        public void setxOffset(int xOffset) {
            this.xOffset = xOffset;
        }

        public int getyOffset() {
            return yOffset;
        }

        public void setyOffset(int yOffset) {
            this.yOffset = yOffset;
        }
    }

    static public class InputStringEvent {
        private String textToInput;

        public String getTextToInput() {
            return textToInput;
        }

        public void setTextToInput(String textToInput) {
            this.textToInput = textToInput;
        }
    }

    static public class ApplicationServerResponseAwaitEvent {
    }

    public RobotActor() {}
    
    @Autowired
    private RobotWrapper robotWrapper;

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(MouseClickEvent.class, e -> {

                }).match(InputStringEvent.class, e -> {
                    robotWrapper.writeString(e.getTextToInput());
                    getSender().tell(new Object(), getSelf());
                }).match(ApplicationServerResponseAwaitEvent.class, e -> {

                })
                .build();
    }
}
