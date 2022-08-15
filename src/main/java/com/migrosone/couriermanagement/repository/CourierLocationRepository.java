package com.migrosone.couriermanagement.repository;

import com.migrosone.couriermanagement.entity.CourierLocation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourierLocationRepository extends MongoRepository<CourierLocation, String> {

    List<CourierLocation> findByCourierIdAndTimeGreaterThanEqualOrderByTimeAsc(
            String courierId, Long time);
}
