package com.example.demo.weather.service;

import com.example.demo.weather.dto.WeatherDTO;
import com.example.demo.weather.repository.WeatherRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.json.JSONObject;

@Service
public class WeatherService {

    private final WeatherRepository weatherRepository;
    private final RestTemplate restTemplate = new RestTemplate();

    private final String API_KEY = "3d9d58c3101549fd3a37eed43698b7ee";

    public WeatherService(WeatherRepository weatherRepository) {
        this.weatherRepository = weatherRepository;
    }

    public WeatherDTO getWeather(double lat, double lon, String locationName) {
        String url = String.format(
            "https://api.openweathermap.org/data/2.5/weather?lat=%f&lon=%f&appid=%s&units=metric&lang=kr",
            lat, lon, API_KEY
        );

        String json = restTemplate.getForObject(url, String.class);
        JSONObject obj = new JSONObject(json);

        String weather = obj.getJSONArray("weather").getJSONObject(0).getString("description");
        String icon = obj.getJSONArray("weather").getJSONObject(0).getString("icon");
        double temp = obj.getJSONObject("main").getDouble("temp");
        int humidity = obj.getJSONObject("main").getInt("humidity");
        double wind = obj.getJSONObject("wind").getDouble("speed");

        WeatherDTO dto = new WeatherDTO(locationName, weather, temp, humidity, wind, icon);
        weatherRepository.save(dto); // DB 저장
        return dto; // 프론트 응답
    }
}
