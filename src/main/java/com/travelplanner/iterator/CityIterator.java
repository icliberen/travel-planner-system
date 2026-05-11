package com.travelplanner.iterator;

import com.travelplanner.model.City;

/*
 * Iterator Pattern - Iterator role
 * EN: The client can traverse matching cities without seeing the filtering logic.
 * TR: Client filtreleme mantigini bilmeden uygun sehirleri gezebilir.
 */
public interface CityIterator {
    boolean hasNext();

    City next();

    void reset();
}
