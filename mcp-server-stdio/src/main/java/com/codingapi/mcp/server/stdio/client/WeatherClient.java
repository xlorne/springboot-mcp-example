package com.codingapi.mcp.server.stdio.client;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class WeatherClient {

    private static final String BASE_URL = "https://api.weather.gov";

    private final RestClient restClient;

    public WeatherClient() {

        this.restClient = RestClient.builder()
                .baseUrl(BASE_URL)
                .defaultHeader("Accept", "application/geo+json")
                .defaultHeader("User-Agent", "WeatherApiClient/1.0 (your@email.com)")
                .build();
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Points(@JsonProperty("properties") Props properties) {
        @JsonIgnoreProperties(ignoreUnknown = true)
        public record Props(@JsonProperty("forecast") String forecast) {
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Forecast(@JsonProperty("properties") Props properties) {
        @JsonIgnoreProperties(ignoreUnknown = true)
        public record Props(@JsonProperty("periods") List<Period> periods) {
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        public record Period(@JsonProperty("number") Integer number, @JsonProperty("name") String name,
                             @JsonProperty("startTime") String startTime, @JsonProperty("endTime") String endTime,
                             @JsonProperty("isDaytime") Boolean isDayTime, @JsonProperty("temperature") Integer temperature,
                             @JsonProperty("temperatureUnit") String temperatureUnit,
                             @JsonProperty("temperatureTrend") String temperatureTrend,
                             @JsonProperty("probabilityOfPrecipitation") Map probabilityOfPrecipitation,
                             @JsonProperty("windSpeed") String windSpeed, @JsonProperty("windDirection") String windDirection,
                             @JsonProperty("icon") String icon, @JsonProperty("shortForecast") String shortForecast,
                             @JsonProperty("detailedForecast") String detailedForecast) {
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Alert(@JsonProperty("features") List<Feature> features) {

        @JsonIgnoreProperties(ignoreUnknown = true)
        public record Feature(@JsonProperty("properties") Properties properties) {
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        public record Properties(@JsonProperty("event") String event, @JsonProperty("areaDesc") String areaDesc,
                                 @JsonProperty("severity") String severity, @JsonProperty("description") String description,
                                 @JsonProperty("instruction") String instruction) {
        }
    }


    /**
     * Get forecast for a specific latitude/longitude
     * @param latitude Latitude
     * @param longitude Longitude
     * @return The forecast for the given location
     * @throws RestClientException if the request fails
     */
    public String getWeatherForecastByLocation(double latitude, double longitude) {
        var points = restClient.get()
                .uri("/points/{latitude},{longitude}", latitude, longitude)
                .retrieve()
                .body(Points.class);

        assert points != null;
        var forecast = restClient.get().uri(points.properties().forecast()).retrieve().body(Forecast.class);

        assert forecast != null;
        return forecast.properties().periods().stream().map(p -> String.format("""
                %s:
                Temperature: %s %s
                Wind: %s %s
                Forecast: %s
                """, p.name(), p.temperature(), p.temperatureUnit(), p.windSpeed(), p.windDirection(),
                p.detailedForecast())).collect(Collectors.joining());
    }

    /**
     * Get alerts for a specific area
     * @param state Area code. Two-letter US state code (e.g. CA, NY)
     * @return Human readable alert information
     * @throws RestClientException if the request fails
     */
    public String getAlerts(String state) {
        Alert alert = restClient.get().uri("/alerts/active/area/{state}", state).retrieve().body(Alert.class);

        return alert.features()
                .stream()
                .map(f -> String.format("""
					Event: %s
					Area: %s
					Severity: %s
					Description: %s
					Instructions: %s
					""", f.properties().event(), f.properties.areaDesc(), f.properties.severity(),
                        f.properties.description(), f.properties.instruction()))
                .collect(Collectors.joining("\n"));
    }

}
