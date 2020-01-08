package io.helidon.examples.mp.kafka;

import io.helidon.examples.mp.spi.Startup;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

@ApplicationScoped
@Startup
public class KafkaMessageConsumer {
    @Inject
    @ConfigProperty(name = "kafka.broker")
    String broker;
    @Inject @ConfigProperty(name = "kafka.topic")
    String topic;
    @Inject @ConfigProperty(name = "kafka.group")
    String group;

    KafkaConsumer<String, String> consumer;
    Logger logger = Logger.getLogger(this.getClass().getName());

    public KafkaMessageConsumer() {
    }
    @PostConstruct
    private void init(){
        // Create a consumer
        // Configure the consumer
        Properties properties = new Properties();
        // Point it to the brokers
        properties.setProperty("bootstrap.servers", broker);
        // Set the consumer group (all consumers must belong to a group).
        properties.setProperty("group.id", group);
        // Set how to serialize key/value pairs
        properties.setProperty("key.deserializer","org.apache.kafka.common.serialization.StringDeserializer");
        properties.setProperty("value.deserializer","org.apache.kafka.common.serialization.StringDeserializer");
        // When a group is first created, it has no offset stored to start reading from. This tells it to start
        // with the earliest record in the stream.
        properties.setProperty("auto.offset.reset","earliest");

        // specify the protocol for Domain Joined clusters
        //properties.setProperty(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, "SASL_PLAINTEXT");

        consumer = new KafkaConsumer<>(properties);

        // Subscribe to the 'test' topic
        consumer.subscribe(Arrays.asList(topic));
        startKafkaMessageConsumer();
    }

    private void startKafkaMessageConsumer(){
        ExecutorService executorService = Executors.newFixedThreadPool(2);

        executorService.execute(()->{
            logger.info(String.format("starting expensive task thread %s", Thread.currentThread().getName()));
            listenTopic();
        });
    }



    public void listenTopic() {
        while(true) {
            // Poll for records
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(200));
            // Did we get any?
            if (records.count() == 0) {
                // timeout/nothing to read
            } else {
                // Yes, loop over records
                for(ConsumerRecord<String, String> record: records) {
                    // Display record and count
                    logger.info("===========Message Received=============");
                    logger.info(String.format("partition= %s,  offset = %s, value = %s",record.partition(), record.offset(), record.value()));
                }
            }
        }
    }

    @PreDestroy
    public void cleanUp(){
        consumer.close();
    }
    public void dummy(){

    }
}
