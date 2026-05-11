package com.seng324.travelplanner.decorator;

public class ShoppingMallVisit extends ActivityDecorator {
    public ShoppingMallVisit(VisitableCity wrappedPlan) {
        super(wrappedPlan);
    }

    @Override
    protected String activityName() {
        return "Shopping Mall Visit";
    }

    @Override
    protected double activityCost() {
        return 60.0;
    }

    @Override
    protected double activityHours() {
        return 3.0;
    }
}
