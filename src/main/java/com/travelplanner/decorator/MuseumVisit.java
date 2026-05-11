package com.travelplanner.decorator;

public class MuseumVisit extends ActivityDecorator {
    public MuseumVisit(VisitableCity wrappedPlan) {
        super(wrappedPlan);
    }

    @Override
    protected String activityName() {
        return "Museum Visit";
    }

    @Override
    protected double activityCost() {
        return 35.0;
    }

    @Override
    protected double activityHours() {
        return 2.5;
    }
}
