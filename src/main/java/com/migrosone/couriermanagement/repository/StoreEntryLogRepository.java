package com.migrosone.couriermanagement.repository;

import com.migrosone.couriermanagement.entity.StoreEntryLog;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StoreEntryLogRepository extends MongoRepository<StoreEntryLog, String> {

    Optional<StoreEntryLog> findFirstByCourierIdAndTimeGreaterThan(String courierId, Long time);
}
