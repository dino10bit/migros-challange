package com.migrosone.couriermanagement.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

@Data
public class CumulativeDistance {

    @Id private String id;

    private Double distance;

    @Indexed private Long time;
}
