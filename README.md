# Travel Planner System

A Java Swing desktop application demonstrating six GoF design patterns in a travel planning scenario. Browse Turkish cities, sort and filter by weather, build visit plans, and watch live weather updates with charts.

> **Branch `improvements`** — This branch extends the base with modern UI, new features, tests, and CI. See [`main`](../../tree/main) for the original version.

## What's New (improvements branch)

### Modern UI
- **FlatLaf Dark Theme** — Replaces default Swing L&F with a modern dark appearance via [FlatLaf](https://www.formdev.com/flatlaf/)

### New Features
- **City Search** — Real-time filtering of the city list by name as you type
- **Weather Speed Control** — Adjustable slider (1–10 seconds) to control how fast weather updates
- **Trip Export** — Export your planned trip (city info + activities + budget) to a text file
- **Weather History** — Tracks and displays the last 5 weather snapshots per city

### New Design Pattern
- **Factory Pattern** — `CityIteratorFactory` centralizes iterator creation, replacing inline switch logic

### Engineering
- **Maven Build** — `pom.xml` with dependency management, shade plugin for fat JAR
- **JUnit 5 Tests** — 15+ unit tests covering Strategy, Decorator, Iterator, Factory, and Observer
- **GitHub Actions CI** — Automated build and test on every push
- **java.util.logging** — Structured logging throughout the application
- **Javadoc** — Comprehensive documentation on all new and enhanced classes

## Design Patterns (6 total)

| # | Pattern | Key Classes | Purpose |
|---|---------|-------------|---------|
| 1 | **Singleton** | `CityRepository` | One shared city list across the app |
| 2 | **Strategy** | `CitySortStrategy`, `CitySortContext` | Swappable sorting algorithms |
| 3 | **Iterator** | `CityIterator`, `WeatherCityIterator`, weather-specific iterators | Traverse cities matching a weather filter |
| 4 | **Observer** | `WeatherSubject`, `WeatherObserver`, `WeatherReportProvider`, `WeatherHistoryTracker` | Publish weather changes to GUI, charts, and history |
| 5 | **Decorator** | `VisitableCity`, `ActivityDecorator`, activity classes | Stack activities with additive cost/time |
| 6 | **Factory** | `CityIteratorFactory` | Create the correct iterator for a weather state |

## Project Structure

```
├── pom.xml                              # Maven build
├── data/cities.json                     # City data (12 Turkish cities)
├── src/main/java/com/travelplanner/
│   ├── Main.java                        # Entry point (FlatLaf setup)
│   ├── model/                           # City, WeatherState
│   ├── repository/                      # Singleton CityRepository
│   ├── sorting/                         # Strategy pattern
│   ├── iterator/                        # Iterator + Factory patterns
│   ├── observer/                        # Observer + WeatherHistoryTracker
│   ├── decorator/                       # Decorator pattern
│   ├── export/                          # TripExporter utility
│   └── ui/                              # Swing GUI + chart panels
├── src/test/java/com/travelplanner/     # JUnit 5 tests
│   ├── sorting/SortingStrategyTest.java
│   ├── decorator/DecoratorTest.java
│   ├── iterator/IteratorTest.java
│   └── observer/ObserverTest.java
├── .github/workflows/ci.yml            # GitHub Actions CI
└── uml/                                 # Mermaid diagrams
```

## Requirements

- **JDK 21+**
- **Maven 3.9+** (or use the included Maven wrapper)

## Build & Run

```bash
# Build
mvn clean package

# Run
java -jar target/travel-planner-system-2.0.0.jar

# Run tests
mvn test
```

## How to Use

1. **Search** — Type in the search box to filter cities by name
2. **Sort** — Choose a sorting criterion from the dropdown
3. **Filter by weather** — Select a weather condition
4. **Plan activities** — Check activities to see total budget and duration
5. **Adjust weather speed** — Drag the slider to change update frequency
6. **Export** — Click "Export Plan" to save your trip plan to a file
7. **Watch history** — Weather snapshots appear in the planner panel

## License

MIT License — see [LICENSE](LICENSE).
