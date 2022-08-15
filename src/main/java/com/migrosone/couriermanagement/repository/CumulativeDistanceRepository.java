package com.migrosone.couriermanagement.repository;

import com.migrosone.couriermanagement.entity.CumulativeDistance;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CumulativeDistanceRepository extends MongoRepository<CumulativeDistance, String> {}
