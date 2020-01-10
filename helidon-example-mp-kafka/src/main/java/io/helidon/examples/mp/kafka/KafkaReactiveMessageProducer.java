package io.helidon.examples.mp.kafka;

import io.smallrye.reactive.messaging.kafka.KafkaMessage;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.reactive.messaging.Outgoing;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Logger;

@ApplicationScoped
public class KafkaReactiveMessageProducer {
    private static final Logger LOGGER = Logger.getLogger(KafkaReactiveMessageProducer.class.getName());
    private BlockingQueue<String> messages = new LinkedBlockingQueue<>();
    @Inject
    @ConfigProperty(name = "smallrye.messaging.source.kafka.topic")
    private String topic;

    public void putIntoMessagingQueue(String message) {
        messages.add(message);
    }
        /*
    @Outgoing("data")
    public KafkaMessage<String, String> send() {
        try {
            String message = messages.take();
            LOGGER.info("Sending message to kafka with the message: " + message);
            return KafkaMessage.of(topic, "key", message);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
*/
    @Outgoing("data")
    public CompletionStage<KafkaMessage<String, String>> send() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                String message = messages.take();
                LOGGER.info("Sending message to kafka with the message: " + message);
                return KafkaMessage.of(topic, "key", message);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
    }



}
