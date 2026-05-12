package com.travelplanner.decorator;

import com.travelplanner.model.City;
import com.travelplanner.model.WeatherState;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DecoratorTest {
    private final City testCity = new City("TestCity", 100000, 500, 20, WeatherState.SUNNY);

    @Test
    void basicPlan_hasZeroCostAndHours() {
        VisitableCity plan = new BasicCityPlan(testCity);
        assertEquals(0.0, plan.getCost());
        assertEquals(0.0, plan.getRequiredHours());
        assertEquals("TestCity", plan.getCityName());
    }

    @Test
    void singleDecorator_addsCostAndHours() {
        VisitableCity plan = new MuseumVisit(new BasicCityPlan(testCity));
        assertEquals(35.0, plan.getCost());
        assertEquals(2.5, plan.getRequiredHours());
    }

    @Test
    void multipleDecorators_accumulateCostsAndHours() {
        VisitableCity plan = new BasicCityPlan(testCity);
        plan = new MuseumVisit(plan);       // +35, +2.5h
        plan = new ShoppingMallVisit(plan);  // +60, +3h
        plan = new ParkVisit(plan);          // +15, +1.5h
        plan = new CityCenterVisit(plan);    // +25, +2h

        assertEquals(135.0, plan.getCost());
        assertEquals(9.0, plan.getRequiredHours());
    }

    @Test
    void decorator_preservesCityName() {
        VisitableCity plan = new ParkVisit(new MuseumVisit(new BasicCityPlan(testCity)));
        assertEquals("TestCity", plan.getCityName());
    }

    @Test
    void decorator_buildsPlanDescription() {
        VisitableCity plan = new BasicCityPlan(testCity);
        plan = new MuseumVisit(plan);
        plan = new ParkVisit(plan);
        String desc = plan.getPlanDescription();
        assertTrue(desc.contains("Museum Visit"));
        assertTrue(desc.contains("Park Visit"));
    }
}
