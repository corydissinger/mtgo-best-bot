package com.cd.bot.client.wrapper.ws;

import com.cd.bot.model.domain.bot.LifecycleEvent;
import com.cd.bot.model.domain.bot.LifecycleEventOutcome;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.stomp.*;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.lang.reflect.Type;
import java.util.function.Consumer;

/**
 * Created by Cory on 5/16/2017.
 */
@Service
public class BotWebSocketClient {

    private static final Logger log = LoggerFactory.getLogger(BotWebSocketClient.class);

    @Autowired
    private WebSocketStompClient webSocketStompClient;

    @Autowired
    private String botClientUrl;

    public void sendLifecycleEvent(final LifecycleEvent event, final Consumer<LifecycleEventOutcome> outcomeConsumer) {
        String url = botClientUrl + "/client-bot";
        StompSessionHandler sessionHandler = new LifecycleEventSessionHandler(event, outcomeConsumer);
        webSocketStompClient.connect(url, sessionHandler);
    }

    public class LifecycleEventSessionHandler extends StompSessionHandlerAdapter {

        private final LifecycleEvent event;
        private final Consumer<LifecycleEventOutcome> outcomeConsumer;


        public LifecycleEventSessionHandler(final LifecycleEvent event, final Consumer<LifecycleEventOutcome> outcomeConsumer) {
            super();
            this.event = event;
            this.outcomeConsumer = outcomeConsumer;
        }

        @Override
        public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
            session.send("/client-bot", event);
            session.subscribe("client-bot", new StompFrameHandler() {
                @Override
                public Type getPayloadType(StompHeaders stompHeaders) {
                    return LifecycleEventOutcome.class;
                }

                @Override
                public void handleFrame(StompHeaders stompHeaders, Object o) {
                    LifecycleEventOutcome outcome = (LifecycleEventOutcome) o;
                    outcomeConsumer.accept(outcome);
                }
            });
        }
    }
}
