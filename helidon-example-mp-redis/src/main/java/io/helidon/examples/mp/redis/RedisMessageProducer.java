package io.helidon.examples.mp.redis;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.pubsub.StatefulRedisPubSubConnection;
import io.lettuce.core.pubsub.api.async.RedisPubSubAsyncCommands;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.logging.Level;
import java.util.logging.Logger;

@ApplicationScoped
public class RedisMessageProducer {
    @Inject
    @ConfigProperty(name = "redis.host")
    String server;
    @Inject @ConfigProperty(name = "redis.topic")
    String topic;
    @Inject @ConfigProperty(name = "redis.port")
    String port;
    RedisClient redisClient;
    Logger logger = Logger.getLogger(this.getClass().getName());

    @PostConstruct
    public void init() {
        redisClient = RedisClient.create(RedisURI.Builder.redis(server, Integer.valueOf(port)).build());
    }


    public void publishMessage(String message) {
        StatefulRedisPubSubConnection<String, String> connection
                = redisClient.connectPubSub();
        RedisPubSubAsyncCommands<String, String> async
                = connection.async();
        async.publish(topic, message);
        logger.log(Level.INFO, "Publishing message: {0} to channel: {1} ", new Object[]{ message, "channel"});
    }
    @PreDestroy
    public void cleanUp(){
        redisClient.shutdown();
    }

}
