package com.example.demo.weather.controller;

import com.example.demo.weather.dto.WeatherDTO;
import com.example.demo.weather.service.WeatherService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@Controller
@RequestMapping("/weather")
public class WeatherController {

    private static final String API_KEY = "3d9d58c3101549fd3a37eed43698b7ee";

    
    @GetMapping("/current")
    public String getCurrentWeather(@RequestParam double lat, @RequestParam double lon, Model model) {
        String apiUrl = String.format(
            "https://api.openweathermap.org/data/2.5/weather?lat=%f&lon=%f&units=metric&lang=kr&appid=%s",
            lat, lon, API_KEY
        );

        RestTemplate restTemplate = new RestTemplate();
        Map<String, Object> response = restTemplate.getForObject(apiUrl, Map.class);

        if (response != null) {
            Map<String, Object> main = (Map<String, Object>) response.get("main");
            Map<String, Object> wind = (Map<String, Object>) response.get("wind");
            List<Map<String, Object>> weatherList = (List<Map<String, Object>>) response.get("weather");
            Map<String, Object> weather = weatherList.get(0);

            model.addAttribute("locationName", response.get("name"));
            model.addAttribute("weather", weather.get("description"));
            model.addAttribute("temp", main.get("temp"));
            model.addAttribute("humidity", main.get("humidity"));
            model.addAttribute("wind", wind.get("speed"));
            model.addAttribute("icon", weather.get("icon"));
        }
        return "weather/weather";  // 날씨 정보를 담은 JSP 페이지로 이동
    }
    
    @GetMapping("/weather")
    public String showWeather(Model model) {
    	return "weather/weather";
    }
    
    @GetMapping("/search")
    @ResponseBody
    public List<Map<String, Object>> searchCity(@RequestParam("q") String query) {
        String url = String.format(
            "https://secure.geonames.org/searchJSON?q=%s&maxRows=5&username=sukyung",
            query
        );

        RestTemplate restTemplate = new RestTemplate();
        Map<String, Object> response = restTemplate.getForObject(url, Map.class);
        List<Map<String, Object>> suggestions = new java.util.ArrayList<>();

        if (response != null && response.containsKey("geonames")) {
            List<Map<String, Object>> geonames = (List<Map<String, Object>>) response.get("geonames");

            for (Map<String, Object> city : geonames) {
                Map<String, Object> cityInfo = new HashMap<>();
                cityInfo.put("name", city.get("name"));
                cityInfo.put("country", city.get("countryName"));
                cityInfo.put("lat", city.get("lat"));
                cityInfo.put("lng", city.get("lng"));
                suggestions.add(cityInfo);
            }
        }

        return suggestions;
    }	
    @RequestMapping("/example")
    public String exampleMethod(@RequestParam(name = "lat", required = false) Double lat, 
                                 @RequestParam(name = "lon", required = false) Double lon) {
        if (lat == null || lon == null) {
            return "Error: lat and lon are required!";
        }
        // lat, lon 사용
        return "success";
    }
	/*
	 * @GetMapping("/current") public WeatherDTO
	 * getCurrentWeather(@RequestParam("lat") double lat,
	 * 
	 * @RequestParam("lon") double lon) { String locationName = "위도 " + lat +
	 * ", 경도 " + lon; return weatherService.getWeather(lat, lon, locationName); }
	 */
}
