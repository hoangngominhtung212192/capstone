package com.tks.gwa.utils;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;

import java.io.IOException;

public class GoogleMapHelper {
    private static final String API_KEY = "AIzaSyASrDg7CwB0GQ_XGzWfbkP4RUE_31YCy1M";
    private static final GeoApiContext context = new GeoApiContext.Builder().apiKey(API_KEY).build();
    private static final int R = 6378137; // R is earthâ€™s radius (mean radius = 6,371km)

    public static LatLng getLatLngFromAddress(String address) {
        LatLng p = null;
        try {
            GeocodingResult[] results = GeocodingApi.geocode(context, address).await();
            p = results[0].geometry.location;
        } catch (ApiException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return p;
    }

    /**
     * This is the implementation Haversine Distance Algorithm between two places
     * R = earthâ€™s radius (mean radius = 6,371km)
     * Î”lat = lat2âˆ’ lat1
     * Î”long = long2âˆ’ long1
     * a = sinÂ²(Î”lat/2) + cos(lat1).cos(lat2).sinÂ²(Î”long/2)
     * c = 2.atan2(âˆša, âˆš(1âˆ’a))
     * d = R.c
     */

    public static long calculateDistanceBetweenTwoPoint(LatLng from, LatLng to) {
        Double latDistance = toRad(to.lat - from.lat);
        Double lngDistance = toRad(to.lng - from.lng);
        Double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) +
                Math.cos(toRad(from.lat)) * Math.cos(toRad(to.lat)) *
                        Math.sin(lngDistance / 2) * Math.sin(lngDistance / 2);
        Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        Double distance = R * c;

        return Math.round(distance);
    }

    private static Double toRad(Double value) {
        return value * Math.PI / 180;
    }
}
