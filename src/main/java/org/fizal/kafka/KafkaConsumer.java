package org.fizal.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaConsumer {

    @KafkaListener(topics="fizaltopic1", groupId = "foo")
    public void receive(String message){
        System.out.println("received payload = " + message);
    }
}
