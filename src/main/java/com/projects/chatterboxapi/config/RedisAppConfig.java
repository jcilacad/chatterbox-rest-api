package com.projects.chatterboxapi.config;

import com.projects.chatterboxapi.service.RedisMessageSubscriber;
import com.projects.chatterboxapi.utils.AppConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

@Configuration
public class RedisAppConfig {
    private static final Logger logger = LoggerFactory.getLogger(RedisAppConfig.class);
    @Value("${redis.endpoint.url}")
    private String redisEndpointUrl;
    @Value("${redis.password}")
    private String redisPassword;

    @Bean
    @ConditionalOnMissingBean(name = "redisTemplate")
    @Primary
    public StringRedisTemplate redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        return new StringRedisTemplate(redisConnectionFactory);
    }

    @Bean
    MessageListenerAdapter messageListenerAdapter() {
        return new MessageListenerAdapter(new RedisMessageSubscriber());
    }

    @Bean
    RedisMessageListenerContainer redisContainer(RedisConnectionFactory redisConnectionFactory,
                                                 MessageListenerAdapter messageListenerAdapter) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisConnectionFactory);
        container.addMessageListener(messageListenerAdapter, topic());
        return container;
    }

    @Bean
    ChannelTopic topic() {
        return new ChannelTopic("MESSAGES");
    }

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        String[] urlParts = redisEndpointUrl.split(":");
        String host = urlParts[0];
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration(host, Integer.parseInt(AppConstants.PORT));
        logger.info("Connecting to %s:%s with password: %s%n", host, AppConstants.PORT, redisPassword);
        if (redisPassword != null) {
            config.setPassword(redisPassword);
        }
        return new LettuceConnectionFactory(config);
    }
}
