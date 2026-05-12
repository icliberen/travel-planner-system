package com.travelplanner.sorting;

import com.travelplanner.model.City;
import com.travelplanner.model.WeatherState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SortingStrategyTest {
    private List<City> cities;

    @BeforeEach
    void setUp() {
        cities = List.of(
                new City("Istanbul", 16000000, 5343, 18, WeatherState.SUNNY),
                new City("Ankara", 5700000, 25632, 12, WeatherState.CLOUDY),
                new City("Izmir", 4400000, 11891, 22, WeatherState.SUNNY)
        );
    }

    @Test
    void sortByName_returnsAlphabeticalOrder() {
        CitySortContext ctx = new CitySortContext(new SortByNameStrategy());
        List<City> sorted = ctx.sort(cities);
        assertEquals("Ankara", sorted.get(0).getName());
        assertEquals("Istanbul", sorted.get(1).getName());
        assertEquals("Izmir", sorted.get(2).getName());
    }

    @Test
    void sortByPopulation_returnsDescendingOrder() {
        CitySortContext ctx = new CitySortContext(new SortByPopulationStrategy());
        List<City> sorted = ctx.sort(cities);
        assertEquals("Istanbul", sorted.get(0).getName());
        assertEquals("Ankara", sorted.get(1).getName());
    }

    @Test
    void sortByArea_returnsDescendingOrder() {
        CitySortContext ctx = new CitySortContext(new SortByAreaStrategy());
        List<City> sorted = ctx.sort(cities);
        assertEquals("Ankara", sorted.get(0).getName());
    }

    @Test
    void changeStrategy_producesNewOrder() {
        CitySortContext ctx = new CitySortContext(new SortByNameStrategy());
        assertEquals("Ankara", ctx.sort(cities).get(0).getName());

        ctx.setStrategy(new SortByPopulationStrategy());
        assertEquals("Istanbul", ctx.sort(cities).get(0).getName());
    }
}
