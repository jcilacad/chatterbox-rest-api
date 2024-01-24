package com.projects.chatterboxapi.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Function;

public class RedisMessageSubscriber implements MessageListener {
    private static final Logger logger = LoggerFactory.getLogger(RedisMessageSubscriber.class);

    CopyOnWriteArrayList<Function<String, Integer>> handlers = new CopyOnWriteArrayList<>();

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String messageBody = new String(message.getBody());

        logger.debug("Received message in global subscriber: " + message.toString());
        handlers.forEach(handler -> handler.apply(messageBody));
    }

    public void attach(Function<String, Integer> handler) {
        handlers.add(handler);
    }

    public void detach(Function<String, Integer> handler) {
        handlers.removeIf(e -> e.equals(handler));
    }
}
