package com.migrosone.couriermanagement.messaging;

import com.migrosone.couriermanagement.entity.CourierLocation;
import com.migrosone.couriermanagement.service.LogService;
import lombok.extern.log4j.Log4j2;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.util.List;

@Log4j2
@Component
public class LogServiceLocationUpdatedMessageConsumer {

    private final LogService logService;

    public LogServiceLocationUpdatedMessageConsumer(LogService logService) {
        this.logService = logService;
    }

    @KafkaListener(
            topicPattern = "${spring.kafka.template.location-updated-topic.name}",
            containerFactory = "logServiceKafkaListenerContainerFactory")
    public void consume(List<CourierLocation> messages, Acknowledgment acknowledgment) {

        logService.logStoreEntries(messages);

        messages.forEach(
                message ->
                        log.info(
                                "message=Courier location update message consumed. consumer=LogServiceLocationUpdatedMessageConsumer, courierId={}, time={}",
                                message.getCourierId(),
                                message.getTime()));
        acknowledgment.acknowledge();
    }
}
