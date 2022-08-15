package com.migrosone.couriermanagement.service;

import com.migrosone.couriermanagement.BaseIntegrationTestConfig;
import com.migrosone.couriermanagement.entity.CumulativeDistance;
import com.migrosone.couriermanagement.repository.CumulativeDistanceRepository;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;

import java.time.Instant;
import java.util.Optional;

@AutoConfigureDataMongo
public class DistanceServiceITest extends BaseIntegrationTestConfig {

    public static final String COURIER_ID = "courier1";
    public static final double SOME_DISTANCE = 55d;
    public static final double SOME_OTHER_DISTANCE = 10d;

    @Autowired private CumulativeDistanceRepository cumulativeDistanceRepository;

    @Autowired private DistanceService distanceService;

    @Before
    public void before() {
        MockitoAnnotations.openMocks(this);
    }

    @After
    public void teardown() {
        cumulativeDistanceRepository.deleteAll();
    }

    @Test
    public void shouldReturnZeroWhenNoRecordExist() {
        Double result = distanceService.getTotalDistanceByCourierId(COURIER_ID);

        Assert.assertNotNull(result);
        Assert.assertEquals(0, result.compareTo(0D));
    }

    @Test
    public void shouldReturnValueWhenRecordExist() {
        CumulativeDistance cumulativeDistance = new CumulativeDistance();
        cumulativeDistance.setId(COURIER_ID);
        cumulativeDistance.setDistance(SOME_DISTANCE);
        cumulativeDistance.setTime(Instant.now().getEpochSecond());

        cumulativeDistanceRepository.save(cumulativeDistance);

        Double result = distanceService.getTotalDistanceByCourierId(COURIER_ID);

        Assert.assertNotNull(result);
        Assert.assertEquals(0, result.compareTo(SOME_DISTANCE));
    }

    @Test
    public void shouldUpdateRecordWhenRecordExist() {
        CumulativeDistance cumulativeDistance = new CumulativeDistance();
        cumulativeDistance.setId(COURIER_ID);
        cumulativeDistance.setDistance(SOME_DISTANCE);
        cumulativeDistance.setTime(Instant.now().getEpochSecond());

        cumulativeDistanceRepository.save(cumulativeDistance);

        distanceService.updateTotalDistance(COURIER_ID, SOME_OTHER_DISTANCE);

        Optional<CumulativeDistance> result = cumulativeDistanceRepository.findById(COURIER_ID);

        Assert.assertTrue(result.isPresent());
        Assert.assertEquals(
                0, result.get().getDistance().compareTo(SOME_DISTANCE + SOME_OTHER_DISTANCE));
    }

    @Test
    public void shouldCreateRecordWhenRecordNotExist() {
        distanceService.updateTotalDistance(COURIER_ID, SOME_OTHER_DISTANCE);

        Optional<CumulativeDistance> result = cumulativeDistanceRepository.findById(COURIER_ID);

        Assert.assertTrue(result.isPresent());
        Assert.assertEquals(0, result.get().getDistance().compareTo(SOME_OTHER_DISTANCE));
    }
}
