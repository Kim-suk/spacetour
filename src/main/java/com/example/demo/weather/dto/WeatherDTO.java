package com.example.demo.weather.dto;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "weather")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WeatherDTO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String locationName;
    private String weather;
    private double temp;
    private int humidity;
    private double wind;
    private String icon;

    public WeatherDTO(String locationName, String weather, double temp, int humidity, double wind, String icon) {
        this.locationName = locationName;
        this.weather = weather;
        this.temp = temp;
        this.humidity = humidity;
        this.wind = wind;
        this.icon = icon;
    }
}
