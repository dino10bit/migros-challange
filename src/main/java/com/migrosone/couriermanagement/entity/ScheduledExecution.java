package com.migrosone.couriermanagement.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

import javax.validation.constraints.NotNull;

@Data
public class ScheduledExecution {

    @Id private String id;

    @NotNull @Indexed private String serviceName;

    @NotNull private Long time;
}
