package com.travelplanner.iterator;

import com.travelplanner.model.City;
import com.travelplanner.model.WeatherState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class IteratorTest {
    private List<City> cities;

    @BeforeEach
    void setUp() {
        cities = new ArrayList<>(List.of(
                new City("Istanbul", 16000000, 5343, 18, WeatherState.SUNNY),
                new City("Ankara", 5700000, 25632, 12, WeatherState.CLOUDY),
                new City("Izmir", 4400000, 11891, 22, WeatherState.SUNNY),
                new City("Trabzon", 800000, 4685, 5, WeatherState.RAINY),
                new City("Erzurum", 750000, 25355, -3, WeatherState.SNOWY)
        ));
    }

    @Test
    void sunnyIterator_returnsOnlySunnyCities() {
        CityIterator it = CityIteratorFactory.create(cities, WeatherState.SUNNY);
        int count = 0;
        while (it.hasNext()) {
            assertEquals(WeatherState.SUNNY, it.next().getCurrentWeatherState());
            count++;
        }
        assertEquals(2, count);
    }

    @Test
    void cloudyIterator_returnsOnlyCloudyCities() {
        CityIterator it = CityIteratorFactory.create(cities, WeatherState.CLOUDY);
        assertTrue(it.hasNext());
        assertEquals("Ankara", it.next().getName());
        assertFalse(it.hasNext());
    }

    @Test
    void snowyIterator_returnsOnlySnowyCities() {
        CityIterator it = CityIteratorFactory.create(cities, WeatherState.SNOWY);
        assertTrue(it.hasNext());
        assertEquals("Erzurum", it.next().getName());
        assertFalse(it.hasNext());
    }

    @Test
    void factory_createsIteratorForAllStates() {
        for (WeatherState state : WeatherState.values()) {
            assertNotNull(CityIteratorFactory.create(cities, state));
        }
    }

    @Test
    void collection_createIterator_matchesFactory() {
        WeatherFilteredCityCollection collection = new WeatherFilteredCityCollection(cities);
        CityIterator fromCollection = collection.createIterator(WeatherState.RAINY);
        CityIterator fromFactory = CityIteratorFactory.create(cities, WeatherState.RAINY);

        List<String> collNames = new ArrayList<>();
        while (fromCollection.hasNext()) collNames.add(fromCollection.next().getName());
        List<String> factNames = new ArrayList<>();
        while (fromFactory.hasNext()) factNames.add(fromFactory.next().getName());

        assertEquals(collNames, factNames);
    }
}
