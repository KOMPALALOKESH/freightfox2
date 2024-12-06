package com.example.freightfox2.service;

import com.example.pincodeapi.model.Route;
import com.example.pincodeapi.repository.RouteRepository;
import com.example.pincodeapi.service.GoogleMapsService;
import com.example.pincodeapi.service.RouteService;
import com.google.maps.model.DirectionsLeg;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.Duration;
import com.google.maps.model.Distance;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class RouteServiceTest {

    @Test
    public void testGetRoute_WhenCached() throws Exception {
        // Mock dependencies
        RouteRepository routeRepository = mock(RouteRepository.class);
        GoogleMapsService googleMapsService = mock(GoogleMapsService.class);
        RouteService routeService = new RouteService();
        routeService.setRouteRepository(routeRepository);
        routeService.setGoogleMapsService(googleMapsService);

        // Mock route data
        Route cachedRoute = new Route();
        cachedRoute.setFromPincode("141106");
        cachedRoute.setToPincode("110060");
        cachedRoute.setDistance("250 km");
        cachedRoute.setDuration("5 hours");
        cachedRoute.setRouteDetails("Via NH44");

        when(routeRepository.findByFromPincodeAndToPincode("141106", "110060"))
                .thenReturn(Optional.of(cachedRoute));

        // Test
        Route result = routeService.getRoute("141106", "110060");

        // Assertions
        assertNotNull(result);
        assertEquals("250 km", result.getDistance());
        assertEquals("5 hours", result.getDuration());
        assertEquals("Via NH44", result.getRouteDetails());
        verify(googleMapsService, never()).getDirections(anyString(), anyString()); // Ensure Google Maps API is not called
    }

    @Test
    public void testGetRoute_WhenNotCached() throws Exception {
        // Mock dependencies
        RouteRepository routeRepository = mock(RouteRepository.class);
        GoogleMapsService googleMapsService = mock(GoogleMapsService.class);
        RouteService routeService = new RouteService();
        routeService.setRouteRepository(routeRepository);
        routeService.setGoogleMapsService(googleMapsService);

        // Mock DirectionsResult from Google Maps API
        DirectionsLeg mockLeg = new DirectionsLeg();
        mockLeg.distance = new Distance("250 km", 250000);
        mockLeg.duration = new Duration("5 hours", 18000);
        DirectionsRoute mockRoute = new DirectionsRoute();
        mockRoute.legs = new DirectionsLeg[]{mockLeg};
        mockRoute.summary = "Via NH44";
        DirectionsResult mockDirectionsResult = new DirectionsResult();
        mockDirectionsResult.routes = new DirectionsRoute[]{mockRoute};

        when(routeRepository.findByFromPincodeAndToPincode("141106", "110060"))
                .thenReturn(Optional.empty());
        when(googleMapsService.getDirections("141106", "110060"))
                .thenReturn(mockDirectionsResult);

        // Test
        Route result = routeService.getRoute("141106", "110060");

        // Assertions
        assertNotNull(result);
        assertEquals("250 km", result.getDistance());
        assertEquals("5 hours", result.getDuration());
        assertEquals("Via NH44", result.getRouteDetails());
        verify(routeRepository).save(any(Route.class)); // Ensure the route is saved in the database
    }
}
