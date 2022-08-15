package com.migrosone.couriermanagement.service;

import com.migrosone.couriermanagement.entity.CumulativeDistance;
import com.migrosone.couriermanagement.repository.CumulativeDistanceRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@Log4j2
@Service
public class DistanceService {

    private final CumulativeDistanceRepository cumulativeDistanceRepository;

    public DistanceService(CumulativeDistanceRepository cumulativeDistanceRepository) {
        this.cumulativeDistanceRepository = cumulativeDistanceRepository;
    }

    public Double getTotalDistanceByCourierId(String courierId) {
        Optional<CumulativeDistance> distance = cumulativeDistanceRepository.findById(courierId);

        if (distance.isPresent()) {
            log.info(
                    "Total distance found. courierId={}, totalDistance={}",
                    courierId,
                    distance.get().getDistance());
            return distance.get().getDistance();
        } else {
            log.info("There is no distance record found. courierId={}", courierId);
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

            log.info(
                    "Total distance updated. courierId={}, newDistance={}",
                    courierId,
                    existingDistance.getDistance());
        } else {
            CumulativeDistance newDistance = new CumulativeDistance();
            newDistance.setId(courierId);
            newDistance.setDistance(distance);
            newDistance.setTime(Instant.now().getEpochSecond());
            cumulativeDistanceRepository.save(newDistance);

            log.info(
                    "Total distance updated. courierId={}, newDistance={}",
                    courierId,
                    newDistance.getDistance());
        }
    }
}
