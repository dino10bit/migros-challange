package com.migrosone.couriermanagement.util;

import com.migrosone.couriermanagement.entity.CourierLocation;

public class DataGenerationUtil {

    public static CourierLocation createLocation(
            String courierId, Long time, Double lat, Double lng) {
        CourierLocation location = new CourierLocation();

        location.setCourierId(courierId);
        location.setTime(time);
        location.setLatitude(lat);
        location.setLongitude(lng);

        return location;
    }
}
