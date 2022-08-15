package com.migrosone.couriermanagement.service;

import com.migrosone.couriermanagement.entity.CourierLocation;
import com.migrosone.couriermanagement.entity.ScheduledExecution;
import com.migrosone.couriermanagement.enumeration.Unit;
import com.migrosone.couriermanagement.repository.ScheduledExecutionRepository;
import com.migrosone.couriermanagement.util.DistanceCalculator;
import lombok.extern.log4j.Log4j2;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Log4j2
@Service
public class DistanceCalculatorService {

    public static final String SERVICE_NAME = "DistanceCalculatorService";

    private final ScheduledExecutionRepository scheduledExecutionRepository;

    private final LocationService locationService;

    private final DistanceService distanceService;

    public DistanceCalculatorService(
            ScheduledExecutionRepository scheduledExecutionRepository,
            LocationService locationService,
            DistanceService distanceService) {
        this.scheduledExecutionRepository = scheduledExecutionRepository;
        this.locationService = locationService;
        this.distanceService = distanceService;
    }

    @Scheduled(cron = "${cumulative-distance-calculation.cron}")
    @SchedulerLock(name = "calculateDistance")
    public void calculate() {
        Long lastExecutionTime = getLastExecutionTime();

        List<String> courierIds = locationService.findDistinctCourierIdsByTime(lastExecutionTime);

        if (!CollectionUtils.isEmpty(courierIds)) {
            ExecutorService executorService = Executors.newFixedThreadPool(courierIds.size());

            List<Callable<Void>> distanceUpdateTasks = new ArrayList<>();

            try {
                courierIds.forEach(
                        courierId ->
                                distanceUpdateTasks.add(
                                        getDistanceUpdateTask(courierId, lastExecutionTime)));

                executorService.invokeAll(distanceUpdateTasks);
            } catch (InterruptedException e) {
                log.error("exception occured while writing files with full category writers");
            }
        }
        updateLastExecutionTime(Instant.now().getEpochSecond());
    }

    private Callable<Void> getDistanceUpdateTask(String courierId, Long lastExecutionTime) {
        return () -> {
            List<CourierLocation> locations =
                    locationService.findLocationsByCourierIdAndTime(courierId, lastExecutionTime);

            double totalDistance = 0D;
            if (!locations.isEmpty() && locations.size() > 1) {
                for (int i = 1; i < locations.size(); i++) {
                    totalDistance +=
                            DistanceCalculator.distance(
                                    locations.get(i - 1).getLatitude(),
                                    locations.get(i - 1).getLongitude(),
                                    locations.get(i).getLatitude(),
                                    locations.get(i).getLongitude(),
                                    Unit.KILOMETER);
                }

                distanceService.updateTotalDistance(courierId, totalDistance);
            }
            return null;
        };
    }

    private Long getLastExecutionTime() {
        Optional<ScheduledExecution> optionalScheduledExecution =
                scheduledExecutionRepository.findFirstByServiceName(SERVICE_NAME);

        if (optionalScheduledExecution.isPresent()) {
            return optionalScheduledExecution.get().getTime();
        } else {
            return Instant.MIN.getEpochSecond();
        }
    }

    private void updateLastExecutionTime(Long time) {
        Optional<ScheduledExecution> optionalScheduledExecution =
                scheduledExecutionRepository.findFirstByServiceName(SERVICE_NAME);

        if (optionalScheduledExecution.isPresent()) {
            ScheduledExecution existingRecord = optionalScheduledExecution.get();
            existingRecord.setTime(time);
        } else {
            ScheduledExecution record = new ScheduledExecution();
            record.setServiceName(SERVICE_NAME);
            record.setTime(time);
            scheduledExecutionRepository.save(record);
        }
    }
}
