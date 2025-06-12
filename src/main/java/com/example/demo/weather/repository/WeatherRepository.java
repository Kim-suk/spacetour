package com.example.demo.weather.repository;

import com.example.demo.weather.dto.WeatherDTO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WeatherRepository extends JpaRepository<WeatherDTO, Long> {
    // 기본적인 CRUD 제공
}
