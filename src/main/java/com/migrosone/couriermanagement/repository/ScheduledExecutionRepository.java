package com.migrosone.couriermanagement.repository;

import com.migrosone.couriermanagement.entity.ScheduledExecution;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ScheduledExecutionRepository extends MongoRepository<ScheduledExecution, String> {

    Optional<ScheduledExecution> findFirstByServiceName(String serviceName);
}
