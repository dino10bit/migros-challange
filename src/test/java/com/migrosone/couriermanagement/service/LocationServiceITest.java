package com.migrosone.couriermanagement.service;

import com.migrosone.couriermanagement.BaseIntegrationTestConfig;
import com.migrosone.couriermanagement.entity.CourierLocation;
import com.migrosone.couriermanagement.repository.CourierLocationRepository;
import com.migrosone.couriermanagement.util.DataGenerationUtil;
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
public class LocationServiceITest extends BaseIntegrationTestConfig {

    public static final String COURIER_ID = "courier1";
    public static final String COURIER_ID_2 = "courier2";

    @Autowired private CourierLocationRepository locationRepository;

    @Autowired private LocationService locationService;

    @Before
    public void before() {
        MockitoAnnotations.openMocks(this);
    }

    @After
    public void teardown() {
        locationRepository.deleteAll();
    }

    @Test
    public void shouldSaveAllLocationsWhenDataIsValid() {
        List<CourierLocation> locations = new ArrayList<>();

        locations.add(
                DataGenerationUtil.createLocation(
                        COURIER_ID, Instant.now().getEpochSecond(), 40.986106, 29.1161293));

        locations.add(
                DataGenerationUtil.createLocation(
                        COURIER_ID, Instant.now().getEpochSecond() + 2, 41.986106, 29.1161293));

        locationService.createNewLocations(locations);

        List<CourierLocation> result = locationRepository.findAll();

        Assert.assertNotNull(result);
        Assert.assertFalse(result.isEmpty());
        Assert.assertEquals(2, result.size());
    }

    @Test
    public void ShouldReturnDistinctCourierIdsWhenRecordsExist() {
        List<CourierLocation> locations = new ArrayList<>();

        long now = Instant.now().getEpochSecond();

        locations.add(DataGenerationUtil.createLocation(COURIER_ID, now, 40.986106, 29.1161293));

        locations.add(
                DataGenerationUtil.createLocation(COURIER_ID, now + 2, 41.986106, 29.1161293));

        locations.add(DataGenerationUtil.createLocation(COURIER_ID_2, now, 41.986106, 29.1161293));

        locations.add(
                DataGenerationUtil.createLocation(COURIER_ID_2, now + 2, 42.986106, 29.1161293));

        locationRepository.saveAll(locations);

        List<String> courierIds = locationService.findDistinctCourierIdsByTime(now - 10);

        Assert.assertNotNull(courierIds);
        Assert.assertFalse(courierIds.isEmpty());
        Assert.assertEquals(2, courierIds.size());
        Assert.assertTrue(courierIds.contains(COURIER_ID));
        Assert.assertTrue(courierIds.contains(COURIER_ID_2));
    }

    @Test
    public void ShouldReturnAllLocationsInTimeRangeWhenRecordsExist() {
        List<CourierLocation> locations = new ArrayList<>();

        long now = Instant.now().getEpochSecond();

        locations.add(DataGenerationUtil.createLocation(COURIER_ID, now, 40.986106, 29.1161293));

        locations.add(
                DataGenerationUtil.createLocation(COURIER_ID, now + 2, 41.986106, 29.1161293));

        locations.add(
                DataGenerationUtil.createLocation(COURIER_ID, now + 4, 41.986106, 29.1161293));

        locations.add(
                DataGenerationUtil.createLocation(COURIER_ID_2, now + 2, 42.986106, 29.1161293));

        locationRepository.saveAll(locations);

        List<CourierLocation> result =
                locationService.findLocationsByCourierIdAndTime(COURIER_ID, now + 1);

        Assert.assertNotNull(result);
        Assert.assertFalse(result.isEmpty());
        Assert.assertEquals(2, result.size());
    }
}
