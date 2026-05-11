package com.seng324.travelplanner.decorator;

public class ParkVisit extends ActivityDecorator {
    public ParkVisit(VisitableCity wrappedPlan) {
        super(wrappedPlan);
    }

    @Override
    protected String activityName() {
        return "Park Visit";
    }

    @Override
    protected double activityCost() {
        return 15.0;
    }

    @Override
    protected double activityHours() {
        return 1.5;
    }
}
