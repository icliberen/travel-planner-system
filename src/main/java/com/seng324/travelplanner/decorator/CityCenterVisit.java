package com.seng324.travelplanner.decorator;

public class CityCenterVisit extends ActivityDecorator {
    public CityCenterVisit(VisitableCity wrappedPlan) {
        super(wrappedPlan);
    }

    @Override
    protected String activityName() {
        return "Historic City Center Visit";
    }

    @Override
    protected double activityCost() {
        return 25.0;
    }

    @Override
    protected double activityHours() {
        return 2.0;
    }
}
