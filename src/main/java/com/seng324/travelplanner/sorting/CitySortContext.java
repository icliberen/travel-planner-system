package com.seng324.travelplanner.sorting;

import com.seng324.travelplanner.model.City;

import java.util.List;

/*
 * Strategy Pattern - Context role
 * EN: The context owns the current strategy and delegates sorting to it.
 * TR: Context aktif stratejiyi tutar ve siralama isini o stratejiye devreder.
 */
public class CitySortContext {
    private CitySortStrategy strategy;

    public CitySortContext(CitySortStrategy strategy) {
        this.strategy = strategy;
    }

    public void setStrategy(CitySortStrategy strategy) {
        this.strategy = strategy;
    }

    public List<City> sort(List<City> cities) {
        return strategy.sort(cities);
    }
}
