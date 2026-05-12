package com.travelplanner.observer;

import com.travelplanner.model.City;
import com.travelplanner.model.WeatherState;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

/**
 * Observer that records every weather update as a timestamped snapshot.
 * Useful for trend analysis and debugging.
 */
public class WeatherHistoryTracker implements WeatherObserver {
    private static final Logger LOGGER = Logger.getLogger(WeatherHistoryTracker.class.getName());
    private static final DateTimeFormatter FORMAT = DateTimeFormatter.ofPattern("HH:mm:ss");
    private static final int MAX_ENTRIES = 500;

    private final List<WeatherSnapshot> history = new ArrayList<>();

    public record WeatherSnapshot(
            LocalDateTime timestamp,
            String cityName,
            double temperature,
            WeatherState state
    ) {
        @Override
        public String toString() {
            return "[" + timestamp.format(FORMAT) + "] "
                    + cityName + ": " + String.format("%.1f", temperature)
                    + "°C " + state;
        }
    }

    @Override
    public void updateWeather(List<City> cities) {
        LocalDateTime now = LocalDateTime.now();
        for (City city : cities) {
            if (history.size() >= MAX_ENTRIES) {
                history.remove(0);
            }
            history.add(new WeatherSnapshot(
                    now, city.getName(),
                    city.getCurrentTemperature(),
                    city.getCurrentWeatherState()
            ));
        }
        LOGGER.fine("Recorded weather snapshot for " + cities.size() + " cities");
    }

    public List<WeatherSnapshot> getHistory() {
        return Collections.unmodifiableList(history);
    }

    public List<WeatherSnapshot> getHistoryForCity(String cityName) {
        return history.stream()
                .filter(s -> s.cityName().equals(cityName))
                .toList();
    }

    public int getSnapshotCount() {
        return history.size();
    }
}
