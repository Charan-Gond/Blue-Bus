package com.bluebus.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.sql.Time;

@Component
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Route {
    private int roueId;
    private String source;
    private String destination;
    private String routeName;
    private float distance;
}
