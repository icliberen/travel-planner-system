package com.seng324.travelplanner.observer;

/*
 * Observer Pattern - Subject/Publisher role
 * EN: The subject keeps observers and notifies them after each weather update.
 * TR: Subject observer listesini tutar ve her hava guncellemesinden sonra onlari bilgilendirir.
 */
public interface WeatherSubject {
    void attach(WeatherObserver observer);

    void detach(WeatherObserver observer);

    void notifyObservers();
}
