package com.migrosone.couriermanagement.configuration.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.TopicBuilder;

@Profile("!test")
@EnableKafka
@Configuration
public class KafkaConfiguration {

    @Bean
    public NewTopic locationUpdatedTopic(
            @Value(value = "${spring.kafka.template.location-updated-topic.name}")
                    String locationUpdatedTopicName) {
        return TopicBuilder.name(locationUpdatedTopicName).partitions(1).replicas(1).build();
    }
}
