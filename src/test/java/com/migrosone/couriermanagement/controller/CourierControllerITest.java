package com.migrosone.couriermanagement.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.migrosone.couriermanagement.BaseIntegrationTestConfig;
import com.migrosone.couriermanagement.controller.request.UpdateLocationRequest;
import com.migrosone.couriermanagement.messaging.CourierLocationUpdatedProducer;
import com.migrosone.couriermanagement.service.DistanceService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.Instant;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@AutoConfigureMockMvc(addFilters = false)
public class CourierControllerITest extends BaseIntegrationTestConfig {

    private static final String COURIER_ID = "courier1";

    @MockBean private DistanceService distanceService;

    @MockBean private CourierLocationUpdatedProducer producer;

    @Autowired private MockMvc mvc;

    @Autowired private ObjectMapper objectMapper;

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void shouldReturnOkWhenTotalDistanceRequested() throws Exception {
        Mockito.when(distanceService.getTotalDistanceByCourierId(Mockito.anyString()))
                .thenReturn(0D);

        mvc.perform(
                        MockMvcRequestBuilders.get(
                                        "/courier/{courierId}/total-distance", COURIER_ID)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().json("{\"courierId\":\"courier1\", \"distance\":0}"));
    }

    @Test
    public void shouldReturnOkWhenNewLocationReceived() throws Exception {
        Mockito.doNothing().when(producer).sendMessage(Mockito.any());

        UpdateLocationRequest request = new UpdateLocationRequest();
        request.setCourier(COURIER_ID);
        request.setTime(Instant.now().getEpochSecond());
        request.setLat(40.9923307);
        request.setLng(29.1244229);

        mvc.perform(
                        MockMvcRequestBuilders.post("/courier/{courierId}/location", COURIER_ID)
                                .param("courierId", COURIER_ID)
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());

        Mockito.verify(producer, Mockito.times(1)).sendMessage(Mockito.any());
    }

    @Test
    public void shouldReturnBadRequestWhenTimeMissing() throws Exception {
        Mockito.doNothing().when(producer).sendMessage(Mockito.any());

        UpdateLocationRequest request = new UpdateLocationRequest();
        request.setCourier(COURIER_ID);
        request.setLat(40.9923307);
        request.setLng(29.1244229);

        mvc.perform(
                        MockMvcRequestBuilders.post("/courier/{courierId}/location", COURIER_ID)
                                .param("courierId", COURIER_ID)
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        Mockito.verify(producer, Mockito.never()).sendMessage(Mockito.any());
    }

    @Test
    public void shouldReturnBadRequestWhenLatMissing() throws Exception {
        Mockito.doNothing().when(producer).sendMessage(Mockito.any());

        UpdateLocationRequest request = new UpdateLocationRequest();
        request.setCourier(COURIER_ID);
        request.setTime(Instant.now().getEpochSecond());
        request.setLng(29.1244229);

        mvc.perform(
                        MockMvcRequestBuilders.post("/courier/{courierId}/location", COURIER_ID)
                                .param("courierId", COURIER_ID)
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        Mockito.verify(producer, Mockito.never()).sendMessage(Mockito.any());
    }

    @Test
    public void shouldReturnBadRequestWhenLngMissing() throws Exception {
        Mockito.doNothing().when(producer).sendMessage(Mockito.any());

        UpdateLocationRequest request = new UpdateLocationRequest();
        request.setCourier(COURIER_ID);
        request.setTime(Instant.now().getEpochSecond());
        request.setLat(40.9923307);

        mvc.perform(
                        MockMvcRequestBuilders.post("/courier/{courierId}/location", COURIER_ID)
                                .param("courierId", COURIER_ID)
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        Mockito.verify(producer, Mockito.never()).sendMessage(Mockito.any());
    }
}
