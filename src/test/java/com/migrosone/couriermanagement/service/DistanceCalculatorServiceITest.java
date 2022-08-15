package com.migrosone.couriermanagement.service;

import com.migrosone.couriermanagement.BaseIntegrationTestConfig;
import com.migrosone.couriermanagement.entity.CourierLocation;
import com.migrosone.couriermanagement.entity.CumulativeDistance;
import com.migrosone.couriermanagement.entity.ScheduledExecution;
import com.migrosone.couriermanagement.enumeration.Unit;
import com.migrosone.couriermanagement.repository.CourierLocationRepository;
import com.migrosone.couriermanagement.repository.CumulativeDistanceRepository;
import com.migrosone.couriermanagement.repository.ScheduledExecutionRepository;
import com.migrosone.couriermanagement.util.DataGenerationUtil;
import com.migrosone.couriermanagement.util.DistanceCalculator;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@AutoConfigureDataMongo
public class DistanceCalculatorServiceITest extends BaseIntegrationTestConfig {

    public static final String COURIER_ID = "courier1";
    public static final String COURIER_ID_2 = "courier2";

    @Autowired private ScheduledExecutionRepository scheduledExecutionRepository;

    @Autowired private CourierLocationRepository locationRepository;

    @Autowired private CumulativeDistanceRepository cumulativeDistanceRepository;

    @Autowired private DistanceCalculatorService distanceCalculatorService;

    @Before
    public void before() {
        MockitoAnnotations.openMocks(this);
    }

    @After
    public void teardown() {
        scheduledExecutionRepository.deleteAll();
        locationRepository.deleteAll();
        cumulativeDistanceRepository.deleteAll();
    }

    @Test
    public void shouldCalculateAllWhenNoExecutionTimeFound() {
        long now = Instant.now().getEpochSecond();

        List<CourierLocation> locations = new ArrayList<>();
        locations.add(
                DataGenerationUtil.createLocation(COURIER_ID, now - 2, 40.986106, 29.1161293));
        locations.add(DataGenerationUtil.createLocation(COURIER_ID, now, 41.986106, 29.1161293));
        locations.add(
                DataGenerationUtil.createLocation(COURIER_ID_2, now - 2, 41.986106, 29.1161293));
        locations.add(DataGenerationUtil.createLocation(COURIER_ID_2, now, 42.986106, 29.1161293));

        locationRepository.saveAll(locations);

        distanceCalculatorService.calculate();

        List<ScheduledExecution> executionRecords = scheduledExecutionRepository.findAll();

        Assert.assertNotNull(executionRecords);
        Assert.assertFalse(executionRecords.isEmpty());
        Assert.assertEquals(1, executionRecords.size());

        List<CumulativeDistance> distanceRecords = cumulativeDistanceRepository.findAll();

        Assert.assertNotNull(distanceRecords);
        Assert.assertFalse(distanceRecords.isEmpty());
        Assert.assertEquals(2, distanceRecords.size());
    }

    @Test
    public void shouldCalculateOnlyNewWhenLastExecutionTimeExist() {
        long now = Instant.now().getEpochSecond();

        ScheduledExecution execution = new ScheduledExecution();
        execution.setServiceName("DistanceCalculatorService");
        execution.setTime(now - 3);

        scheduledExecutionRepository.save(execution);

        List<CourierLocation> locations = new ArrayList<>();
        locations.add(
                DataGenerationUtil.createLocation(COURIER_ID, now - 4, 40.986106, 29.1161293));
        locations.add(
                DataGenerationUtil.createLocation(COURIER_ID, now - 2, 41.986106, 29.1161293));
        locations.add(
                DataGenerationUtil.createLocation(COURIER_ID, now - 1, 42.986106, 29.1161293));

        locationRepository.saveAll(locations);

        distanceCalculatorService.calculate();

        List<ScheduledExecution> executionRecords = scheduledExecutionRepository.findAll();

        Assert.assertNotNull(executionRecords);
        Assert.assertFalse(executionRecords.isEmpty());
        Assert.assertEquals(1, executionRecords.size());

        List<CumulativeDistance> distanceRecords = cumulativeDistanceRepository.findAll();

        Assert.assertNotNull(distanceRecords);
        Assert.assertFalse(distanceRecords.isEmpty());
        Assert.assertEquals(1, distanceRecords.size());

        double distance =
                DistanceCalculator.distance(
                        41.986106, 29.1161293, 42.986106, 29.1161293, Unit.KILOMETER);

        Assert.assertEquals(0, distanceRecords.get(0).getDistance().compareTo(distance));
    }
}
