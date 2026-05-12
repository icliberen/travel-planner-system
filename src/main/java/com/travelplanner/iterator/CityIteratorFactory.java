package com.travelplanner.iterator;

import com.travelplanner.model.City;
import com.travelplanner.model.WeatherState;

import java.util.List;

/**
 * Factory Pattern — centralizes iterator creation logic.
 * Replaces the switch expression in WeatherFilteredCityCollection
 * with an extensible factory method.
 */
public final class CityIteratorFactory {

    private CityIteratorFactory() { }

    /**
     * Create the appropriate weather iterator for the given state.
     *
     * @param cities the city list to iterate over
     * @param state  the weather condition to filter by
     * @return a CityIterator that yields only matching cities
     */
    public static CityIterator create(List<City> cities, WeatherState state) {
        return switch (state) {
            case SUNNY  -> new SunnyCityIterator(cities);
            case CLOUDY -> new CloudyCityIterator(cities);
            case RAINY  -> new RainyCityIterator(cities);
            case SNOWY  -> new SnowyCityIterator(cities);
        };
    }
}
