package io.helidon.examples.mp.redis;

import io.helidon.examples.mp.spi.Startup;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Default;
import javax.inject.Inject;
import java.util.logging.Logger;

@Startup
@ApplicationScoped
@Default
public class RedisProvider {
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

    public RedisClient getRedisClient(){
        return redisClient;
    }

    public String getChannelTopic(){
        return topic;
    }

    @PreDestroy
    public void cleanUp(){
        redisClient.shutdown();
    }

}
