package com.example.freightfox2.service;

import com.google.maps.GeoApiContext;
import com.google.maps.DirectionsApi;
import com.google.maps.model.DirectionsResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class GoogleMapsService {

    @Value("${google.maps.api-key}")
    private String apiKey;

    public DirectionsResult getDirections(String from, String to) throws Exception {
        GeoApiContext context = new GeoApiContext.Builder()
                .apiKey(apiKey)
                .build();

        return DirectionsApi.getDirections(context, from, to).await();
    }
}

