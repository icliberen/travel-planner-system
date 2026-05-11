package com.seng324.travelplanner.decorator;

/*
 * Decorator Pattern - Component role
 * EN: The planner works with this abstraction instead of modifying City directly.
 * TR: Planner City sinifini dogrudan degistirmek yerine bu soyutlama ile calisir.
 */
public interface VisitableCity {
    String getCityName();

    String getPlanDescription();

    double getCost();

    double getRequiredHours();
}
