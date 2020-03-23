package io.helidon.examples.mp.redis;

import io.lettuce.core.RedisClient;
import io.lettuce.core.pubsub.StatefulRedisPubSubConnection;
import io.lettuce.core.pubsub.api.async.RedisPubSubAsyncCommands;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.logging.Level;
import java.util.logging.Logger;

@ApplicationScoped
public class RedisMessageProducer {

   Logger logger = Logger.getLogger(this.getClass().getName());
    @Inject
    RedisProvider redisProvider;


    public void publishMessage(String message) {
        RedisClient redisClient = redisProvider.getRedisClient();
        StatefulRedisPubSubConnection<String, String> connection
                = redisClient.connectPubSub();
        RedisPubSubAsyncCommands<String, String> async
                = connection.async();
        async.publish(redisProvider.getChannelTopic(), message);
        logger.log(Level.INFO, "Publishing message: {0} to channel: {1} ", new Object[]{ message, "channel"});
    }

}
