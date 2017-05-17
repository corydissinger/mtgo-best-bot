package com.cd.bot.client.akka;

import akka.actor.Actor;
import akka.actor.IndirectActorProducer;
import org.springframework.context.ApplicationContext;

/**
 * Created by Cory on 5/13/2017.
 */
public class SpringActorProducer implements IndirectActorProducer {
    private ApplicationContext applicationContext;

    private String beanActorName;

    public SpringActorProducer(ApplicationContext applicationContext,
                               String beanActorName) {
        this.applicationContext = applicationContext;
        this.beanActorName = beanActorName;
    }

    @Override
    public Actor produce() {
        return (Actor) applicationContext.getBean(beanActorName);
    }

    @Override
    public Class<? extends Actor> actorClass() {
        return (Class<? extends Actor>) applicationContext
                .getType(beanActorName);
    }
}
