package com.migrosone.couriermanagement.messaging;

import com.migrosone.couriermanagement.entity.CourierLocation;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class CourierLocationUpdatedProducer {

    private final String topicName;
    private final KafkaTemplate<String, CourierLocation> kafkaTemplate;

    public CourierLocationUpdatedProducer(
            @Value("${spring.kafka.template.location-updated-topic.name}") String topicName,
            KafkaTemplate<String, CourierLocation> kafkaTemplate) {
        this.topicName = topicName;
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(CourierLocation location) {
        try {
            kafkaTemplate.send(topicName, String.valueOf(location.getCourierId()), location);

            log.info(
                    "message=Courier location update message produced to kafka topic. courierId={}, time={}",
                    location.getCourierId(),
                    location.getTime());
        } catch (Exception ex) {
            log.warn(
                    "message=Courier location update message couldn't be produced to kafka topic. courierId={}, time={}",
                    location.getCourierId(),
                    location.getTime());
        }
    }
}
