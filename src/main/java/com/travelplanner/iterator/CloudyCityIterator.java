package com.travelplanner.iterator;

import com.travelplanner.model.City;
import com.travelplanner.model.WeatherState;

import java.util.List;

public class CloudyCityIterator extends WeatherCityIterator {
    public CloudyCityIterator(List<City> cities) {
        super(cities);
    }

    @Override
    protected WeatherState targetWeatherState() {
        return WeatherState.CLOUDY;
    }
}
