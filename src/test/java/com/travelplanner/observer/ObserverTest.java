package com.travelplanner.observer;

import com.travelplanner.model.City;
import com.travelplanner.model.WeatherState;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ObserverTest {
    @Test
    void historyTracker_recordsSnapshots() {
        WeatherHistoryTracker tracker = new WeatherHistoryTracker();
        List<City> cities = List.of(
                new City("A", 1000, 100, 15, WeatherState.SUNNY),
                new City("B", 2000, 200, 10, WeatherState.RAINY)
        );

        tracker.updateWeather(cities);
        assertEquals(2, tracker.getSnapshotCount());

        tracker.updateWeather(cities);
        assertEquals(4, tracker.getSnapshotCount());
    }

    @Test
    void historyTracker_filtersByCity() {
        WeatherHistoryTracker tracker = new WeatherHistoryTracker();
        List<City> cities = List.of(
                new City("Istanbul", 16000000, 5343, 18, WeatherState.SUNNY),
                new City("Ankara", 5700000, 25632, 12, WeatherState.CLOUDY)
        );
        tracker.updateWeather(cities);
        tracker.updateWeather(cities);

        assertEquals(2, tracker.getHistoryForCity("Istanbul").size());
        assertEquals(2, tracker.getHistoryForCity("Ankara").size());
        assertEquals(0, tracker.getHistoryForCity("Nonexistent").size());
    }

    @Test
    void provider_attachDetach() {
        List<City> cities = new ArrayList<>(List.of(
                new City("Test", 1000, 100, 15, WeatherState.SUNNY)
        ));
        WeatherReportProvider provider = new WeatherReportProvider(cities);
        List<List<City>> received = new ArrayList<>();
        WeatherObserver observer = received::add;

        provider.attach(observer);
        provider.notifyObservers();
        assertEquals(1, received.size());

        provider.detach(observer);
        provider.notifyObservers();
        assertEquals(1, received.size()); // not called again
    }
}
