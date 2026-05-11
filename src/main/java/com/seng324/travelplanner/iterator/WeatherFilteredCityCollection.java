package com.seng324.travelplanner.iterator;

import com.seng324.travelplanner.model.City;
import com.seng324.travelplanner.model.WeatherState;

import java.util.List;

/*
 * Iterator Pattern - IterableCollection role
 * EN: The collection creates the correct iterator for the selected weather state.
 * TR: Collection, secilen hava durumuna uygun iterator nesnesini olusturur.
 */
public class WeatherFilteredCityCollection {
    private final List<City> cities;

    public WeatherFilteredCityCollection(List<City> cities) {
        this.cities = cities;
    }

    public CityIterator createIterator(WeatherState state) {
        return switch (state) {
            case SUNNY -> new SunnyCityIterator(cities);
            case CLOUDY -> new CloudyCityIterator(cities);
            case RAINY -> new RainyCityIterator(cities);
            case SNOWY -> new SnowyCityIterator(cities);
        };
    }
}
