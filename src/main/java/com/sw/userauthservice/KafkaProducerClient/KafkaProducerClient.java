package com.sw.userauthservice.KafkaProducerClient;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaProducerClient {
    private KafkaTemplate<String,String> kafkaTemplate;

    public KafkaProducerClient(KafkaTemplate kafkaTemplate){
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(String topicName,String message){
        kafkaTemplate.send(topicName,message);
    }
}
