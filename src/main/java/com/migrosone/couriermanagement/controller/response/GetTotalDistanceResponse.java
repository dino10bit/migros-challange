package com.migrosone.couriermanagement.controller.response;

import lombok.Data;

@Data
public class GetTotalDistanceResponse {

    private String courierId;
    private Double distance; // in kilometers
}
