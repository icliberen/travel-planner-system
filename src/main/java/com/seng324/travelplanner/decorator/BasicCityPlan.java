package com.seng324.travelplanner.decorator;

import com.seng324.travelplanner.model.City;

/*
 * Decorator Pattern - ConcreteComponent role
 * EN: A selected city starts as an empty trip plan with zero extra cost and time.
 * TR: Secilen sehir sifir ek maliyet ve sureye sahip bos bir gezi plani olarak baslar.
 */
public class BasicCityPlan implements VisitableCity {
    private final City city;

    public BasicCityPlan(City city) {
        this.city = city;
    }

    @Override
    public String getCityName() {
        return city.getName();
    }

    @Override
    public String getPlanDescription() {
        return "Base city visit";
    }

    @Override
    public double getCost() {
        return 0.0;
    }

    @Override
    public double getRequiredHours() {
        return 0.0;
    }
}
