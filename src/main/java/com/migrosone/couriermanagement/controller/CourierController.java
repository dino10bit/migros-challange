package com.migrosone.couriermanagement.controller;

import com.migrosone.couriermanagement.controller.request.UpdateLocationRequest;
import com.migrosone.couriermanagement.controller.response.GetTotalDistanceResponse;
import com.migrosone.couriermanagement.entity.CourierLocation;
import com.migrosone.couriermanagement.messaging.CourierLocationUpdatedProducer;
import com.migrosone.couriermanagement.service.DistanceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class CourierController {

    private final CourierLocationUpdatedProducer producer;

    private final DistanceService distanceService;

    public CourierController(
            CourierLocationUpdatedProducer producer, DistanceService distanceService) {
        this.producer = producer;
        this.distanceService = distanceService;
    }

    @PostMapping("/courier/{courierId}/location")
    public ResponseEntity<?> updateLocation(
            @PathVariable String courierId, @RequestBody @Valid UpdateLocationRequest request) {
        CourierLocation location = new CourierLocation();
        location.setCourierId(courierId);
        location.setTime(request.getTime());
        location.setLatitude(request.getLat());
        location.setLongitude(request.getLng());

        producer.sendMessage(location);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/courier/{courierId}/total-distance")
    public ResponseEntity<GetTotalDistanceResponse> getTotalTravelDistance(
            @PathVariable String courierId) {
        Double totalDistance = distanceService.getTotalDistanceByCourierId(courierId);

        GetTotalDistanceResponse response = new GetTotalDistanceResponse();
        response.setCourierId(courierId);
        response.setDistance(totalDistance);

        return ResponseEntity.ok(response);
    }
}
