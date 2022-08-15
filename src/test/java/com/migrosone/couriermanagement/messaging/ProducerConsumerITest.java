package com.migrosone.couriermanagement.messaging;

import com.migrosone.couriermanagement.BaseIntegrationTestConfig;
import com.migrosone.couriermanagement.repository.CourierLocationRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;

@AutoConfigureDataMongo
public class ProducerConsumerITest extends BaseIntegrationTestConfig {

    public static final String COURIER_ID = "courier1";
    public static final String COURIER_ID_2 = "courier2";

    @Autowired
    private CourierLocationRepository locationRepository;

    @Autowired
    private CourierLocationUpdatedProducer producer;

    @Before
    public void before() {
        MockitoAnnotations.openMocks(this);
    }

    @After
    public void teardown() {
        locationRepository.deleteAll();
    }

    @Test
    public void shouldNewRecordBeCreatedWhenMessageSend(){

    }
}
