package com.example.freightfox2.controller;

import com.example.pincodeapi.model.Route;
import com.example.pincodeapi.service.RouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/routes")
public class RouteController {

    @Autowired
    private RouteService routeService;

    @GetMapping
    public Route getRoute(@RequestParam String from, @RequestParam String to) throws Exception {
        return routeService.getRoute(from, to);
    }
}
