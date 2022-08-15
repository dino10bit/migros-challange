package com.migrosone.couriermanagement.configuration;

import com.mongodb.client.MongoClient;
import net.javacrumbs.shedlock.core.LockProvider;
import net.javacrumbs.shedlock.provider.mongo.MongoLockProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@ConditionalOnBean(SchedulerConfiguration.class)
@Configuration
public class ShedLockConfiguration {

    @Bean
    public LockProvider lockProvider(
            @Value(value = "${spring.data.mongodb.database}") String databaseName,
            MongoClient mongo) {
        return new MongoLockProvider(mongo.getDatabase(databaseName));
    }
}
