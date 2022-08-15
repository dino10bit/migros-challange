package com.migrosone.couriermanagement.service;

import com.migrosone.couriermanagement.BaseIntegrationTestConfig;
import com.migrosone.couriermanagement.entity.CourierLocation;
import com.migrosone.couriermanagement.entity.StoreEntryLog;
import com.migrosone.couriermanagement.repository.StoreEntryLogRepository;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;

import java.time.Instant;
import java.util.Collections;
import java.util.List;

@AutoConfigureDataMongo
public class LogServiceITest extends BaseIntegrationTestConfig {

    @Autowired private StoreEntryLogRepository repository;
    @Autowired private LogService logService;

    @Before
    public void before() {
        MockitoAnnotations.openMocks(this);
    }

    @After
    public void teardown() {
        repository.deleteAll();
    }

    @Test
    public void shouldCreateLogWhenCloseToAStore() {
        CourierLocation log = new CourierLocation();
        log.setCourierId("courier1");
        log.setTime(Instant.now().getEpochSecond());
        log.setLatitude(40.986106);
        log.setLongitude(29.1161293);

        logService.logStoreEntries(Collections.singletonList(log));

        List<StoreEntryLog> result = repository.findAll();

        Assert.assertNotNull(result);
        Assert.assertFalse(result.isEmpty());
        Assert.assertEquals(1, result.size());
    }

    @Test
    public void shouldNotCreateLogWhenNotCloseToAnyStore() {
        CourierLocation log = new CourierLocation();
        log.setCourierId("courier1");
        log.setTime(Instant.now().getEpochSecond());
        log.setLatitude(45.986106);
        log.setLongitude(29.1161293);

        logService.logStoreEntries(Collections.singletonList(log));

        List<StoreEntryLog> result = repository.findAll();

        Assert.assertNotNull(result);
        Assert.assertTrue(result.isEmpty());
    }
}
