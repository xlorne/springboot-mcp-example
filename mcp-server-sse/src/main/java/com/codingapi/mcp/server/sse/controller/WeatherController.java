package com.codingapi.mcp.server.sse.controller;

import com.codingapi.mcp.server.sse.client.WeatherClient;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class WeatherController {

    private final WeatherClient weatherClient;

    @RequestMapping("/forecast")
    public String getWeatherForecastByLocation(double latitude, double longitude) {
        return weatherClient.getWeatherForecastByLocation(latitude, longitude);
    }
}
