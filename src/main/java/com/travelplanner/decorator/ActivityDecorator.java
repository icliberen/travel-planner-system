package com.travelplanner.decorator;

/*
 * Decorator Pattern - Decorator role
 * EN: Every activity wraps another VisitableCity and adds its own behavior.
 * TR: Her aktivite baska bir VisitableCity nesnesini sarar ve kendi davranisini ekler.
 */
public abstract class ActivityDecorator implements VisitableCity {
    private final VisitableCity wrappedPlan;

    protected ActivityDecorator(VisitableCity wrappedPlan) {
        this.wrappedPlan = wrappedPlan;
    }

    protected VisitableCity wrappedPlan() {
        return wrappedPlan;
    }

    @Override
    public String getCityName() {
        return wrappedPlan.getCityName();
    }

    @Override
    public String getPlanDescription() {
        return wrappedPlan.getPlanDescription() + " + " + activityName();
    }

    @Override
    public double getCost() {
        return wrappedPlan.getCost() + activityCost();
    }

    @Override
    public double getRequiredHours() {
        return wrappedPlan.getRequiredHours() + activityHours();
    }

    protected abstract String activityName();

    protected abstract double activityCost();

    protected abstract double activityHours();
}
