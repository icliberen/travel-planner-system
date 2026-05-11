package com.seng324.travelplanner.iterator;

import com.seng324.travelplanner.model.City;
import com.seng324.travelplanner.model.WeatherState;

import java.util.List;

public class RainyCityIterator extends WeatherCityIterator {
    public RainyCityIterator(List<City> cities) {
        super(cities);
    }

    @Override
    protected WeatherState targetWeatherState() {
        return WeatherState.RAINY;
    }
}
