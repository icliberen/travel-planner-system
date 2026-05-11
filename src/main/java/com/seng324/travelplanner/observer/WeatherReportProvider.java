package com.seng324.travelplanner.observer;

import com.seng324.travelplanner.model.City;
import com.seng324.travelplanner.model.WeatherState;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

/*
 * Observer Pattern - ConcreteSubject role
 * EN: This publisher changes weather every 3 seconds on its own thread and notifies all observers.
 * TR: Bu publisher kendi thread'i uzerinde 3 saniyede bir havayi degistirir ve tum observerlari uyarir.
 */
public class WeatherReportProvider implements WeatherSubject, Runnable {
    private static final int UPDATE_DELAY_MILLIS = 3000;

    private final List<City> cities;
    private final List<WeatherObserver> observers = new CopyOnWriteArrayList<>();
    private final Random random = new Random();
    private volatile boolean running;
    private Thread workerThread;

    public WeatherReportProvider(List<City> cities) {
        this.cities = cities;
    }

    public void start() {
        if (running) {
            return;
        }
        running = true;
        workerThread = new Thread(this, "WeatherReportProvider");
        workerThread.setDaemon(true);
        workerThread.start();
    }

    public void stop() {
        running = false;
        if (workerThread != null) {
            workerThread.interrupt();
        }
    }

    @Override
    public void run() {
        while (running) {
            try {
                Thread.sleep(UPDATE_DELAY_MILLIS);
                randomizeWeather();
                notifyObservers();
            } catch (InterruptedException interruptedException) {
                Thread.currentThread().interrupt();
                running = false;
            }
        }
    }

    @Override
    public void attach(WeatherObserver observer) {
        observers.add(observer);
    }

    @Override
    public void detach(WeatherObserver observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        List<City> snapshot = new ArrayList<>(cities);
        for (WeatherObserver observer : observers) {
            observer.updateWeather(snapshot);
        }
    }

    private void randomizeWeather() {
        WeatherState[] states = WeatherState.values();
        for (City city : cities) {
            double current = city.getCurrentTemperature();
            double delta = -4.0 + random.nextDouble() * 8.0;
            double nextTemperature = clamp(current + delta, -15.0, 42.0);
            WeatherState nextState = states[random.nextInt(states.length)];
            city.updateWeather(nextTemperature, nextState);
        }
    }

    private double clamp(double value, double minimum, double maximum) {
        return Math.max(minimum, Math.min(maximum, value));
    }
}
