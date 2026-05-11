package com.travelplanner.observer;

import com.travelplanner.model.City;

import java.util.List;

/*
 * Observer Pattern - Observer role
 * EN: Subscribers implement this interface to react when weather data changes.
 * TR: Subscriber siniflar hava verisi degistiginde tepki vermek icin bu arayuzu uygular.
 */
public interface WeatherObserver {
    void updateWeather(List<City> cities);
}
