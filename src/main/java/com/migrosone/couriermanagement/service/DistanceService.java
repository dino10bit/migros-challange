package com.migrosone.couriermanagement.service;

import com.migrosone.couriermanagement.entity.CumulativeDistance;
import com.migrosone.couriermanagement.repository.CumulativeDistanceRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@Service
public class DistanceService {

    private final CumulativeDistanceRepository cumulativeDistanceRepository;

    public DistanceService(CumulativeDistanceRepository cumulativeDistanceRepository) {
        this.cumulativeDistanceRepository = cumulativeDistanceRepository;
    }

    public Double getTotalDistanceByCourierId(String courierId) {
        Optional<CumulativeDistance> distance = cumulativeDistanceRepository.findById(courierId);

        if (distance.isPresent()) {
            return distance.get().getDistance();
        } else {
            return 0D;
        }
    }

    public void updateTotalDistance(String courierId, Double distance) {
        Optional<CumulativeDistance> optionalDistance =
                cumulativeDistanceRepository.findById(courierId);

        if (optionalDistance.isPresent()) {
            CumulativeDistance existingDistance = optionalDistance.get();
            existingDistance.setDistance(existingDistance.getDistance() + distance);
            existingDistance.setTime(Instant.now().getEpochSecond());
            cumulativeDistanceRepository.save(existingDistance);
        } else {
            CumulativeDistance newDistance = new CumulativeDistance();
            newDistance.setId(courierId);
            newDistance.setDistance(distance);
            newDistance.setTime(Instant.now().getEpochSecond());
            cumulativeDistanceRepository.save(newDistance);
        }
    }
}
