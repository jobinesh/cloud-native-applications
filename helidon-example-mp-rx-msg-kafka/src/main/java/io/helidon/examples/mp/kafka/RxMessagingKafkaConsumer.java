package io.helidon.examples.mp.kafka;

import io.smallrye.reactive.messaging.kafka.KafkaMessage;
import io.smallrye.reactive.messaging.kafka.MessageHeaders;
import org.eclipse.microprofile.reactive.messaging.Incoming;

import javax.enterprise.context.ApplicationScoped;
import java.util.concurrent.CompletionStage;
import java.util.logging.Logger;

@ApplicationScoped
public class RxMessagingKafkaConsumer {

    private static final Logger logger = Logger.getLogger(RxMessagingKafkaConsumer.class.getName());
    @Incoming("data")
    public CompletionStage<Void> consume(KafkaMessage<String, String> message) {
        String payload = message.getPayload();
        String key = message.getKey();
        MessageHeaders headers = message.getHeaders();
        Integer partition = message.getPartition();
        Long timestamp = message.getTimestamp();
        logger.info("Received: " + payload + " from topic " + message.getTopic() +" in partition "+partition);
        return message.ack();
    }
}