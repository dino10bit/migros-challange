package com.migrosone.couriermanagement.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.migrosone.couriermanagement.dto.Store;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.List;

public class StoreServiceTest {

    private ObjectMapper objectMapper = new ObjectMapper();

    private StoreService underTest;

    @Before
    public void setup() throws IOException {
        MockitoAnnotations.openMocks(this);

        underTest = new StoreService(objectMapper);
    }

    @Test
    public void shouldLoadStoresFromFile() {

        List<Store> stores = underTest.getStores();

        Assert.assertNotNull(stores);
        Assert.assertFalse(stores.isEmpty());
    }
}
