package com.seng324.travelplanner.iterator;

import com.seng324.travelplanner.model.City;
import com.seng324.travelplanner.model.WeatherState;

import java.util.List;
import java.util.NoSuchElementException;

/*
 * Iterator Pattern - ConcreteIterator base
 * EN: Each subclass chooses one weather condition; this base class stores traversal state.
 * TR: Her alt sinif bir hava durumunu secer; bu temel sinif gezinme durumunu tutar.
 */
public abstract class WeatherCityIterator implements CityIterator {
    private final List<City> cities;
    private int index;

    protected WeatherCityIterator(List<City> cities) {
        this.cities = cities;
    }

    protected abstract WeatherState targetWeatherState();

    @Override
    public boolean hasNext() {
        while (index < cities.size()) {
            if (cities.get(index).getCurrentWeatherState() == targetWeatherState()) {
                return true;
            }
            index++;
        }
        return false;
    }

    @Override
    public City next() {
        if (!hasNext()) {
            throw new NoSuchElementException("No more cities for " + targetWeatherState());
        }
        return cities.get(index++);
    }

    @Override
    public void reset() {
        index = 0;
    }
}
