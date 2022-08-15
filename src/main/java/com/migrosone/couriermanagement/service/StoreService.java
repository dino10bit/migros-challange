package com.migrosone.couriermanagement.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.migrosone.couriermanagement.dto.Store;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

@Service
public class StoreService {

    @Getter private final List<Store> stores;

    public StoreService(ObjectMapper objectMapper) throws IOException {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("stores.json")) {
            Store[] storeArray = objectMapper.readValue(is, Store[].class);

            this.stores = Arrays.asList(storeArray);
        }
    }
}
