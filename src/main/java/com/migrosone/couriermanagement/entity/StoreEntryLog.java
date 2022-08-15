package com.migrosone.couriermanagement.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

@Data
public class StoreEntryLog {

    @Id private String id;

    @Indexed private String courierId;

    private String storeName;

    @Indexed private Long time;

    private Double longitude;

    private Double latitude;
}
