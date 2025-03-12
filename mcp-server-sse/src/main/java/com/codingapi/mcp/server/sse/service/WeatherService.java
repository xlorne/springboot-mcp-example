package com.codingapi.mcp.server.sse.service;

import com.codingapi.mcp.server.sse.client.WeatherClient;
import lombok.AllArgsConstructor;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class WeatherService {

    private final WeatherClient weatherClient;

    @Tool(description = "Get weather forecast for a specific latitude/longitude")
    public String getWeatherForecastByLocation(double latitude, double longitude) {
        return weatherClient.getWeatherForecastByLocation(latitude, longitude);
    }

    @Tool(description = "Get weather alerts for a US state. Input is Two-letter US state code (e.g. CA, NY)")
    public String getAlerts(@ToolParam(description = "Two-letter US state code (e.g. CA, NY") String state) {
        return weatherClient.getAlerts(state);
    }


}
