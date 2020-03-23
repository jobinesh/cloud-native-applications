package io.helidon.examples.mp.redis;

import io.helidon.examples.mp.spi.Startup;
import io.lettuce.core.RedisClient;
import io.lettuce.core.pubsub.RedisPubSubAdapter;
import io.lettuce.core.pubsub.StatefulRedisPubSubConnection;
import io.lettuce.core.pubsub.api.sync.RedisPubSubCommands;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.logging.Level;
import java.util.logging.Logger;

@Startup
@ApplicationScoped
public class RedisMessageConsumer extends RedisPubSubAdapter<String, String> {
    Logger logger = Logger.getLogger(RedisMessageConsumer.class.getName());


    @Inject
    RedisProvider redisProvider;

    @PostConstruct
    public void init() {
        RedisClient redisClient = redisProvider.getRedisClient();
        StatefulRedisPubSubConnection<String, String> con = redisClient.connectPubSub();
        con.addListener(this);
        RedisPubSubCommands<String, String> sync = con.sync();
        sync.subscribe(redisProvider.getChannelTopic());
        logger.log(Level.INFO, "RedisMessageConsumer::init() is called");


    }

    @Override
    public void message(String channel, String message) {
        logger.log(Level.INFO, "Got  message: {0} on channel: {1}", new Object[]{message, channel});
    }

}