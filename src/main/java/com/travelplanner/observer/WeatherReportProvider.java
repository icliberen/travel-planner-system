package com.travelplanner.observer;

import com.travelplanner.model.City;
import com.travelplanner.model.WeatherState;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Logger;

/**
 * Observer Pattern — ConcreteSubject role.
 * Publishes random weather changes on a background thread.
 * The update interval can be changed at runtime via {@link #setUpdateDelayMillis(int)}.
 */
public class WeatherReportProvider implements WeatherSubject, Runnable {
    private static final Logger LOGGER = Logger.getLogger(WeatherReportProvider.class.getName());
    private static final int DEFAULT_DELAY_MILLIS = 3000;

    private final List<City> cities;
    private final List<WeatherObserver> observers = new CopyOnWriteArrayList<>();
    private final Random random = new Random();
    private volatile boolean running;
    private volatile int updateDelayMillis = DEFAULT_DELAY_MILLIS;
    private Thread workerThread;

    public WeatherReportProvider(List<City> cities) {
        this.cities = cities;
    }

    /**
     * Change the weather update interval at runtime.
     *
     * @param millis delay between updates in milliseconds (minimum 500)
     */
    public void setUpdateDelayMillis(int millis) {
        this.updateDelayMillis = Math.max(500, millis);
        LOGGER.info("Update delay changed to " + this.updateDelayMillis + "ms");
        // Interrupt the sleeping thread so the new interval takes effect immediately
        if (workerThread != null && workerThread.isAlive()) {
            workerThread.interrupt();
        }
    }

    public int getUpdateDelayMillis() {
        return updateDelayMillis;
    }

    public boolean isRunning() {
        return running;
    }

    public void start() {
        if (running) {
            return;
        }
        running = true;
        workerThread = new Thread(this, "WeatherReportProvider");
        workerThread.setDaemon(true);
        workerThread.start();
        LOGGER.info("Weather provider started (interval: " + updateDelayMillis + "ms)");
    }

    public void stop() {
        running = false;
        if (workerThread != null) {
            workerThread.interrupt();
        }
        LOGGER.info("Weather provider stopped");
    }

    @Override
    public void run() {
        while (running) {
            try {
                Thread.sleep(updateDelayMillis);
                randomizeWeather();
                notifyObservers();
            } catch (InterruptedException interruptedException) {
                // If still running, the interrupt was just to change the interval
                if (!running) {
                    Thread.currentThread().interrupt();
                }
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
