package io.helidon.examples.mp.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.IOException;
import java.util.Properties;

@ApplicationScoped
public class KafkaClassicMessageProducer {
    @Inject
    @ConfigProperty(name = "kafka.broker")
    private String broker;
    @Inject
    @ConfigProperty(name = "kafka.topic")
    private String topic;
    @Inject
    @ConfigProperty(name = "kafka.group")
    private String group;

    KafkaProducer<String, String> producer;

    public KafkaClassicMessageProducer() {
    }

    @PostConstruct
    private void init() {
        // Set properties used to configure the producer
        Properties properties = new Properties();
        // Set the brokers (bootstrap servers)
        properties.setProperty("bootstrap.servers", broker);
        // Set how to serialize key/value pairs
        properties.setProperty("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.setProperty("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        producer = new KafkaProducer<>(properties);
    }

    public void publishMessage(String message) throws IOException {

        // Send the message to the topic
        try {
            producer.send(new ProducerRecord<String, String>(topic, message));
            producer.flush();
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new IOException(ex.toString());
        }
    }

    @PreDestroy
    public void cleanUp() {
        producer.close();
    }
}


