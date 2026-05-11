package com.travelplanner.sorting;

import com.travelplanner.model.City;

import java.util.List;

/*
 * Strategy Pattern - Strategy role
 * EN: All sorting algorithms share this interface, so the GUI can switch algorithms at runtime.
 * TR: Tum siralama algoritmalari bu ortak arayuzu kullanir; GUI calisma aninda algoritma degistirebilir.
 */
public interface CitySortStrategy {
    List<City> sort(List<City> cities);

    String getDisplayName();
}
