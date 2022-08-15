package com.migrosone.couriermanagement.service;

import com.migrosone.couriermanagement.entity.CourierLocation;
import com.migrosone.couriermanagement.repository.CourierLocationRepository;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocationService {

    private final MongoTemplate mongoTemplate;

    private final CourierLocationRepository locationRepository;

    public LocationService(
            MongoTemplate mongoTemplate, CourierLocationRepository locationRepository) {
        this.mongoTemplate = mongoTemplate;
        this.locationRepository = locationRepository;
    }

    public void createNewLocations(List<CourierLocation> courierLocations) {
        locationRepository.saveAll(courierLocations);
    }

    public List<String> findDistinctCourierIdsByTime(Long time) {
        Query query = new Query();
        query.addCriteria(Criteria.where("time").gt(time));

        return mongoTemplate.findDistinct(query, "courierId", CourierLocation.class, String.class);
    }

    public List<CourierLocation> findLocationsByCourierIdAndTime(String courierId, Long time) {
        return locationRepository.findByCourierIdAndTimeGreaterThanEqualOrderByTimeAsc(
                courierId, time);
    }
}
