package com.migrosone.couriermanagement.messaging;

import com.migrosone.couriermanagement.entity.CourierLocation;
import com.migrosone.couriermanagement.service.LocationService;
import lombok.extern.log4j.Log4j2;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.util.List;

@Log4j2
@Component
public class LocationServiceLocationUpdatedMessageConsumer {

    private final LocationService locationService;

    public LocationServiceLocationUpdatedMessageConsumer(LocationService locationService) {
        this.locationService = locationService;
    }

    @KafkaListener(
            topicPattern = "${spring.kafka.template.location-updated-topic.name}",
            containerFactory = "locationServiceKafkaListenerContainerFactory")
    public void consume(List<CourierLocation> messages, Acknowledgment acknowledgment) {

        locationService.createNewLocations(messages);

        messages.forEach(
                message ->
                        log.info(
                                "message=Courier location update message consumed. consumer=LocationServiceLocationUpdatedMessageConsumer, courierId={}, time={}",
                                message.getCourierId(),
                                message.getTime()));
        acknowledgment.acknowledge();
    }
}
