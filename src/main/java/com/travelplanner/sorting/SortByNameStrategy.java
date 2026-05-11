package com.travelplanner.sorting;

import com.travelplanner.model.City;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SortByNameStrategy implements CitySortStrategy {
    @Override
    public List<City> sort(List<City> cities) {
        List<City> sorted = new ArrayList<>(cities);
        sorted.sort(Comparator.comparing(City::getName));
        return sorted;
    }

    @Override
    public String getDisplayName() {
        return "Sort by name";
    }

    @Override
    public String toString() {
        return getDisplayName();
    }
}
