package com.migrosone.couriermanagement.configuration.kafka;

import com.migrosone.couriermanagement.entity.CourierLocation;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConsumerConfiguration {

    @Bean
    public ConsumerFactory<String, CourierLocation> locationServiceConsumerFactory(
            @Value(value = "${spring.kafka.bootstrap-servers}") String bootstrapAddress,
            @Value(value = "${spring.kafka.consumer.location-service.group-id}")
                    String locationServiceGroupId) {
        JsonDeserializer<CourierLocation> deserializer =
                new JsonDeserializer<>(CourierLocation.class);
        deserializer.setRemoveTypeHeaders(false);
        deserializer.addTrustedPackages("*");
        deserializer.setUseTypeMapperForKey(true);

        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, locationServiceGroupId);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, deserializer);
        props.put(ErrorHandlingDeserializer.KEY_DESERIALIZER_CLASS, StringDeserializer.class);
        props.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, JsonDeserializer.class);
        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), deserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, CourierLocation>
            locationServiceKafkaListenerContainerFactory(
                    ConsumerFactory<String, CourierLocation> locationServiceConsumerFactory) {
        ConcurrentKafkaListenerContainerFactory<String, CourierLocation> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(locationServiceConsumerFactory);
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);
        factory.setAutoStartup(true);
        return factory;
    }

    @Bean
    public ConsumerFactory<String, CourierLocation> logServiceConsumerFactory(
            @Value(value = "${spring.kafka.bootstrap-servers}") String bootstrapAddress,
            @Value(value = "${spring.kafka.consumer.log-service.group-id}")
                    String logServiceGroupId) {
        JsonDeserializer<CourierLocation> deserializer =
                new JsonDeserializer<>(CourierLocation.class);
        deserializer.setRemoveTypeHeaders(false);
        deserializer.addTrustedPackages("*");
        deserializer.setUseTypeMapperForKey(true);

        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, logServiceGroupId);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, deserializer);
        props.put(ErrorHandlingDeserializer.KEY_DESERIALIZER_CLASS, StringDeserializer.class);
        props.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, JsonDeserializer.class);
        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), deserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, CourierLocation>
            logServiceKafkaListenerContainerFactory(
                    ConsumerFactory<String, CourierLocation> logServiceConsumerFactory) {
        ConcurrentKafkaListenerContainerFactory<String, CourierLocation> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(logServiceConsumerFactory);
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);
        factory.setAutoStartup(true);
        return factory;
    }
}
