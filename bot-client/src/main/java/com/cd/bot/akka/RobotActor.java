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
        private boolean isDouble;

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

        public boolean isDouble() {
            return isDouble;
        }

        public void setDouble(boolean aDouble) {
            isDouble = aDouble;
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

    static public class ApplicationServerResponseAwaitEvent {}

    static public class EventSuccessEvent {}

    public RobotActor() {}
    
    @Autowired
    private RobotWrapper robotWrapper;

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(MouseClickEvent.class, e -> {
                    if(e.isDouble()) {
                        robotWrapper.doubleClickAtLocation(e.getxOffset(), e.getyOffset());
                    } else {
                        robotWrapper.singleClickAtLocation(e.getxOffset(), e.getyOffset());
                    }

                    getSender().tell(new ApplicationServerResponseAwaitEvent(), getSelf());
                }).match(InputStringEvent.class, e -> {
                    robotWrapper.writeString(e.getTextToInput());
                    getSender().tell(new EventSuccessEvent(), getSelf());
                }).match(ApplicationServerResponseAwaitEvent.class, e -> {

                })
                .build();
    }
}
