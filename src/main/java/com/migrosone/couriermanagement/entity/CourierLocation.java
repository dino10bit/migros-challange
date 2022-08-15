package com.migrosone.couriermanagement.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class CourierLocation {

    @Id private String id;

    @NotBlank @Indexed private String courierId;

    @NotNull @Indexed private Long time;

    @NotNull private Double longitude;

    @NotNull private Double latitude;
}
