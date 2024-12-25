package com.bluebus.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.sql.Time;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
public class Bus {
    private int busId;
    private String name;
    private int routeId;
    private int totalSeats;
    private Time departuretime;
    private Time arrivalTime;



}
