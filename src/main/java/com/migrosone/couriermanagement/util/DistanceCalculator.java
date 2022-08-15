package com.migrosone.couriermanagement.util;

import com.migrosone.couriermanagement.enumeration.Unit;

public class DistanceCalculator {

    public static double distance(double lat1, double lon1, double lat2, double lon2, Unit unit) {
        double theta = lon1 - lon2;
        double dist =
                Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2))
                        + Math.cos(deg2rad(lat1))
                                * Math.cos(deg2rad(lat2))
                                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;

        if (Unit.KILOMETER.equals(unit)) {
            dist = dist * 1.609344;
        } else if (Unit.METER.equals(unit)) {
            dist = dist * 1.609344 * 1000;
        }

        return (dist);
    }

    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private static double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }
}
