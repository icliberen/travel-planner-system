# Travel Planner System

A Java Swing desktop application that demonstrates five Gang of Four (GoF) design patterns in a real-world travel planning scenario. The system lets users browse Turkish cities, sort and filter them by weather, and build visit plans with dynamic budget and time calculations — all while live weather updates drive automatic chart refreshes.

## Features

- **City browser** — View 12 Turkish cities with population, area, temperature, and weather data.
- **Sorting** — Sort cities by name, population, or area via a drop-down selector.
- **Weather filtering** — Filter the city list to show only sunny, cloudy, rainy, or snowy cities.
- **Activity planner** — Select a city and check activities (museum, shopping mall, park, city center) to calculate total budget and duration.
- **Live weather updates** — A background thread randomizes city weather every 3 seconds.
- **Charts** — A temperature bar chart and a weather distribution pie chart update automatically with every weather change.

## Design Patterns

| Pattern | Key Classes | Purpose |
| --- | --- | --- |
| **Singleton** | `CityRepository` | Loads `data/cities.json` once and provides a single shared city list. |
| **Strategy** | `CitySortStrategy`, `SortByNameStrategy`, `SortByPopulationStrategy`, `SortByAreaStrategy`, `CitySortContext` | Lets the GUI switch sorting algorithms at runtime. |
| **Iterator** | `CityIterator`, `WeatherCityIterator`, `SunnyCityIterator`, `CloudyCityIterator`, `RainyCityIterator`, `SnowyCityIterator`, `WeatherFilteredCityCollection` | Traverses only cities that match the selected weather condition. |
| **Observer** | `WeatherSubject`, `WeatherObserver`, `WeatherReportProvider`, `TemperatureBarChartPanel`, `WeatherPieChartPanel`, `TravelPlannerFrame` | Publishes weather changes and refreshes all subscribed GUI components. |
| **Decorator** | `VisitableCity`, `BasicCityPlan`, `ActivityDecorator`, `MuseumVisit`, `ShoppingMallVisit`, `ParkVisit`, `CityCenterVisit` | Wraps a base city plan with optional activities, each adding cost and time. |

## Project Structure

```
├── data/
│   └── cities.json                  # Sample city data (12 Turkish cities)
├── src/main/java/com/travelplanner/
│   ├── Main.java                    # Application entry point
│   ├── model/
│   │   ├── City.java                # City data model
│   │   └── WeatherState.java        # Weather enum (SUNNY, CLOUDY, RAINY, SNOWY)
│   ├── repository/
│   │   └── CityRepository.java      # Singleton — loads and holds city data
│   ├── sorting/
│   │   ├── CitySortStrategy.java    # Strategy interface
│   │   ├── SortByNameStrategy.java
│   │   ├── SortByPopulationStrategy.java
│   │   ├── SortByAreaStrategy.java
│   │   └── CitySortContext.java      # Strategy context
│   ├── iterator/
│   │   ├── CityIterator.java        # Iterator interface
│   │   ├── WeatherCityIterator.java # Abstract base iterator
│   │   ├── SunnyCityIterator.java
│   │   ├── CloudyCityIterator.java
│   │   ├── RainyCityIterator.java
│   │   ├── SnowyCityIterator.java
│   │   └── WeatherFilteredCityCollection.java
│   ├── observer/
│   │   ├── WeatherSubject.java      # Subject interface
│   │   ├── WeatherObserver.java     # Observer interface
│   │   └── WeatherReportProvider.java # Concrete subject
│   ├── decorator/
│   │   ├── VisitableCity.java       # Component interface
│   │   ├── BasicCityPlan.java       # Concrete component
│   │   ├── ActivityDecorator.java   # Abstract decorator
│   │   ├── MuseumVisit.java
│   │   ├── ShoppingMallVisit.java
│   │   ├── ParkVisit.java
│   │   └── CityCenterVisit.java
│   └── ui/
│       ├── TravelPlannerFrame.java  # Main GUI (also an Observer)
│       ├── TemperatureBarChartPanel.java
│       └── WeatherPieChartPanel.java
└── uml/
    ├── travel-planner-uml.mmd      # Mermaid class diagram source
    ├── travel-planner-uml.svg       # Exported SVG
    └── travel-planner-uml.pdf       # Exported PDF
```

## Requirements

- **JDK 21** (or any recent JDK/JRE with Swing support)

## How to Build and Run

```bash
# Compile
mkdir -p build/classes
javac -d build/classes $(find src -name "*.java")

# Copy resources
cp -r data build/classes/

# Run
java -cp build/classes com.travelplanner.Main
```

## How to Use

1. **Sort cities** — Use the *Sorting criteria* dropdown to order the city list by name, population, or area.
2. **Filter by weather** — Use the *Weather filter* dropdown to display only cities with a specific weather condition.
3. **Select a city** — Click a city in the *All Cities* list.
4. **Plan activities** — Check one or more activities in the *City Activity Planner* panel to see the total budget and duration.
5. **Watch live updates** — The weather provider updates city weather randomly every 3 seconds; the bar chart and pie chart refresh automatically.

## UML Class Diagram

The Mermaid source is located at `uml/travel-planner-uml.mmd`. You can view or re-export it using [Mermaid Live Editor](https://mermaid.live/) or any Mermaid-compatible tool.

Pre-exported versions are also included:

- `uml/travel-planner-uml.svg`
- `uml/travel-planner-uml.pdf`

## License

This project is licensed under the MIT License — see the [LICENSE](LICENSE) file for details.
