package com.seng324.travelplanner.model;

public enum WeatherState {
    SUNNY("Sunny"),
    CLOUDY("Cloudy"),
    RAINY("Rainy"),
    SNOWY("Snowy");

    private final String displayName;

    WeatherState(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static WeatherState fromString(String value) {
        for (WeatherState state : values()) {
            if (state.name().equalsIgnoreCase(value) || state.displayName.equalsIgnoreCase(value)) {
                return state;
            }
        }
        throw new IllegalArgumentException("Unknown weather state: " + value);
    }

    @Override
    public String toString() {
        return displayName;
    }
}
