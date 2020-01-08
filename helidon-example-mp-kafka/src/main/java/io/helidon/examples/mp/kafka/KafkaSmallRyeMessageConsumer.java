package io.helidon.examples.mp.kafka;

import io.helidon.examples.mp.spi.Startup;
import io.smallrye.reactive.messaging.kafka.KafkaMessage;
import io.smallrye.reactive.messaging.kafka.MessageHeaders;
import org.eclipse.microprofile.reactive.messaging.Incoming;

import javax.enterprise.context.ApplicationScoped;
import java.util.concurrent.CompletionStage;
import java.util.logging.Logger;

@ApplicationScoped
@Startup
public class KafkaSmallRyeMessageConsumer {
    private static final Logger logger = Logger.getLogger(KafkaSmallRyeMessageConsumer.class.getName());
    @Incoming("demoTopic")
    public CompletionStage<Void> consume(KafkaMessage<String, String> message) {
        String payload = message.getPayload();
        String key = message.getKey();
        MessageHeaders headers = message.getHeaders();
        Integer partition = message.getPartition();
        Long timestamp = message.getTimestamp();
        logger.info("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        logger.info("received: " + payload + " from topic " + message.getTopic());
        return message.ack();
    }
}