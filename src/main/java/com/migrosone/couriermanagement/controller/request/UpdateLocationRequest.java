package com.migrosone.couriermanagement.controller.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class UpdateLocationRequest {

    @NotBlank(message = "Courier Id field should not be blank.")
    private String courier;

    @NotNull(message = "Time field should not be empty.")
    private Long time;

    @NotNull(message = "Latitude field should not be empty.")
    private Double lat;

    @NotNull(message = "Longitude field should not be empty.")
    private Double lng;
}
