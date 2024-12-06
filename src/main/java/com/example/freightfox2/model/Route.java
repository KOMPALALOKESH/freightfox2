package com.example.freightfox2.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Route {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String fromPincode;
    private String toPincode;
    private String distance;
    private String duration;
    private String routeDetails;
}

