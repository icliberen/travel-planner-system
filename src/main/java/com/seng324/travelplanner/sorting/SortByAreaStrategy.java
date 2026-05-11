package com.seng324.travelplanner.sorting;

import com.seng324.travelplanner.model.City;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SortByAreaStrategy implements CitySortStrategy {
    @Override
    public List<City> sort(List<City> cities) {
        List<City> sorted = new ArrayList<>(cities);
        sorted.sort(Comparator.comparingDouble(City::getArea).reversed());
        return sorted;
    }

    @Override
    public String getDisplayName() {
        return "Sort by area";
    }

    @Override
    public String toString() {
        return getDisplayName();
    }
}
