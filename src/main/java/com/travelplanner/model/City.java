package com.travelplanner.model;

import java.text.DecimalFormat;

public class City {
    private static final DecimalFormat TEMP_FORMAT = new DecimalFormat("0.0");

    private final String name;
    private final int population;
    private final double area;
    private double currentTemperature;
    private WeatherState currentWeatherState;

    public City(String name, int population, double area, double currentTemperature, WeatherState currentWeatherState) {
        this.name = name;
        this.population = population;
        this.area = area;
        this.currentTemperature = currentTemperature;
        this.currentWeatherState = currentWeatherState;
    }

    public String getName() {
        return name;
    }

    public int getPopulation() {
        return population;
    }

    public double getArea() {
        return area;
    }

    public synchronized double getCurrentTemperature() {
        return currentTemperature;
    }

    public synchronized WeatherState getCurrentWeatherState() {
        return currentWeatherState;
    }

    public synchronized void updateWeather(double currentTemperature, WeatherState currentWeatherState) {
        this.currentTemperature = currentTemperature;
        this.currentWeatherState = currentWeatherState;
    }

    @Override
    public synchronized String toString() {
        return name + " | pop: " + population + " | area: " + TEMP_FORMAT.format(area)
                + " km2 | " + TEMP_FORMAT.format(currentTemperature) + " C | " + currentWeatherState;
    }
}
