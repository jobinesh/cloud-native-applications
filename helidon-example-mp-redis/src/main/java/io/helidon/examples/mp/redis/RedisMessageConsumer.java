package io.helidon.examples.mp.redis;

import io.helidon.examples.mp.spi.Startup;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.pubsub.RedisPubSubAdapter;
import io.lettuce.core.pubsub.StatefulRedisPubSubConnection;
import io.lettuce.core.pubsub.api.sync.RedisPubSubCommands;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.logging.Level;
import java.util.logging.Logger;

@Startup
@ApplicationScoped
public class RedisMessageConsumer extends RedisPubSubAdapter<String, String> {
    Logger logger = Logger.getLogger(RedisMessageConsumer.class.getName());

    @Inject
    @ConfigProperty(name = "redis.host")
    String server;
    @Inject
    @ConfigProperty(name = "redis.topic")
    String topic;
    @Inject
    @ConfigProperty(name = "redis.port")
    String port;

    RedisClient redisClient;

    @PostConstruct
    public void init() {
        redisClient = RedisClient.create(RedisURI.Builder.redis(server, Integer.valueOf(port)).build());
        StatefulRedisPubSubConnection<String, String> con = redisClient.connectPubSub();
        con.addListener(this);
        RedisPubSubCommands<String, String> sync = con.sync();
        sync.subscribe(topic);
        logger.log(Level.INFO, "RedisMessageConsumer::init() is called");


    }

    @Override
    public void message(String channel, String message) {
        logger.log(Level.INFO, "Got  message: {0} on channel: {1}", new Object[]{message, channel});
    }

    @PreDestroy
    public void cleanUp() {
        redisClient.shutdown();
    }
}