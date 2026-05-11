package com.seng324.travelplanner.repository;

import com.seng324.travelplanner.model.City;
import com.seng324.travelplanner.model.WeatherState;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class CityRepository {
    private static final Path DEFAULT_JSON_PATH = Path.of("data", "cities.json");
    private static CityRepository instance;

    private final List<City> cities;

    /*
     * Singleton Pattern
     * EN: The constructor is private so the application can create only one repository.
     * TR: Constructor private tutulur; uygulama boyunca tek bir repository nesnesi kullanılır.
     */
    private CityRepository() {
        this.cities = loadCities();
    }

    /*
     * Singleton Pattern - global access point
     * EN: getInstance() is the single access point used by GUI, strategies, iterators and the weather provider.
     * TR: getInstance() GUI, stratejiler, iteratorlar ve weather provider icin ortak erisim noktasidir.
     */
    public static synchronized CityRepository getInstance() {
        if (instance == null) {
            instance = new CityRepository();
        }
        return instance;
    }

    public List<City> getCities() {
        return Collections.unmodifiableList(cities);
    }

    private List<City> loadCities() {
        String json = readJson();
        List<City> loadedCities = parseCities(json);
        if (loadedCities.isEmpty()) {
            throw new IllegalStateException("No cities found in data/cities.json");
        }
        return loadedCities;
    }

    private String readJson() {
        try {
            if (Files.exists(DEFAULT_JSON_PATH)) {
                return Files.readString(DEFAULT_JSON_PATH, StandardCharsets.UTF_8);
            }

            try (InputStream stream = CityRepository.class.getResourceAsStream("/data/cities.json")) {
                if (stream == null) {
                    throw new IllegalStateException("Cannot find data/cities.json");
                }
                return new String(stream.readAllBytes(), StandardCharsets.UTF_8);
            }
        } catch (IOException exception) {
            throw new IllegalStateException("Cannot read cities JSON file", exception);
        }
    }

    /*
     * EN: This tiny parser is intentionally limited to the provided project JSON shape.
     * TR: Bu kucuk parser proje icin verilen JSON yapisina gore bilincli olarak sinirli tutulmustur.
     */
    private List<City> parseCities(String json) {
        List<City> result = new ArrayList<>();
        Matcher objectMatcher = Pattern.compile("\\{([^{}]+)}", Pattern.DOTALL).matcher(json);

        while (objectMatcher.find()) {
            String object = objectMatcher.group(1);
            String name = readString(object, "name");
            int population = (int) readNumber(object, "population");
            double area = readNumber(object, "area");
            double temperature = readNumber(object, "currentTemperature");
            WeatherState state = WeatherState.fromString(readString(object, "currentWeatherState"));
            result.add(new City(name, population, area, temperature, state));
        }

        return result;
    }

    private String readString(String object, String key) {
        Matcher matcher = Pattern.compile("\"" + Pattern.quote(key) + "\"\\s*:\\s*\"([^\"]*)\"").matcher(object);
        if (!matcher.find()) {
            throw new IllegalArgumentException("Missing string field: " + key);
        }
        return matcher.group(1);
    }

    private double readNumber(String object, String key) {
        Matcher matcher = Pattern.compile("\"" + Pattern.quote(key) + "\"\\s*:\\s*(-?\\d+(?:\\.\\d+)?)").matcher(object);
        if (!matcher.find()) {
            throw new IllegalArgumentException("Missing numeric field: " + key);
        }
        return Double.parseDouble(matcher.group(1));
    }
}
