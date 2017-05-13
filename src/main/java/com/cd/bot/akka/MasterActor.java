package com.cd.bot.akka;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by Cory on 5/13/2017.
 */
public class MasterActor extends AbstractActor {

    @Autowired
    private ActorRef robotRef;

    @Override
    public Receive createReceive() {
        return null;
    }
}
