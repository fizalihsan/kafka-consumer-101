package org.fizal.kafka;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

import java.util.Map;

/**
 * @EnableKafka enables detection of @KafkaListener annotations on any Spring-managed bean in the container.
 * https://docs.spring.io/spring-kafka/api/org/springframework/kafka/annotation/EnableKafka.html
 *
 *
 */
@EnableKafka
@Configuration
public class KafkaConsumerConfig {

    @Value(value = "${kafka.bootstrapAddress}")
    private String bootstrapAddress;

    public ConsumerFactory<String, String> consumerFactory(String groupId){
        System.out.println("Container Factory Created....");

        Map<String, Object> props = Map.of(
                ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress,
                ConsumerConfig.GROUP_ID_CONFIG, groupId,
                ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class,
                ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class,
                ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest"
        );


        return new DefaultKafkaConsumerFactory<>(props);
    }

    public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory(String groupId){
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory(groupId));

        // Only consume messages that contains "hello" in it
        factory.setRecordFilterStrategy(record -> record.value().contains("hello"));
        return factory;
    }

    public ConcurrentKafkaListenerContainerFactory<String, String> fooFactory(){
        System.out.println("Creating 'foo' container factory");
        return kafkaListenerContainerFactory("foo");
    }
}
