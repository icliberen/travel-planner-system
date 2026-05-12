package com.travelplanner.export;

import com.travelplanner.decorator.VisitableCity;
import com.travelplanner.model.City;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Logger;

/**
 * Exports a trip plan to a text file.
 * Demonstrates a simple utility that leverages the Decorator output.
 */
public final class TripExporter {
    private static final Logger LOGGER = Logger.getLogger(TripExporter.class.getName());
    private static final DecimalFormat MONEY = new DecimalFormat("0.00");
    private static final DecimalFormat HOURS = new DecimalFormat("0.0");
    private static final DateTimeFormatter TIMESTAMP = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private TripExporter() { }

    /**
     * Export the given plan for the selected city to a text file.
     *
     * @param city the selected city
     * @param plan the decorated visit plan
     * @param outputPath path to save the file
     * @throws IOException if writing fails
     */
    public static void export(City city, VisitableCity plan, Path outputPath) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("╔══════════════════════════════════════════╗\n");
        sb.append("║         TRAVEL PLAN REPORT               ║\n");
        sb.append("╚══════════════════════════════════════════╝\n\n");
        sb.append("Generated: ").append(LocalDateTime.now().format(TIMESTAMP)).append("\n\n");
        sb.append("── City Information ──────────────────────\n");
        sb.append("  Name        : ").append(city.getName()).append("\n");
        sb.append("  Population  : ").append(city.getPopulation()).append("\n");
        sb.append("  Area        : ").append(HOURS.format(city.getArea())).append(" km²\n");
        sb.append("  Temperature : ").append(HOURS.format(city.getCurrentTemperature())).append(" °C\n");
        sb.append("  Weather     : ").append(city.getCurrentWeatherState()).append("\n\n");
        sb.append("── Visit Plan ───────────────────────────\n");
        sb.append("  Activities  : ").append(plan.getPlanDescription()).append("\n");
        sb.append("  Total Cost  : $").append(MONEY.format(plan.getCost())).append("\n");
        sb.append("  Duration    : ").append(HOURS.format(plan.getRequiredHours())).append(" hours\n\n");
        sb.append("── End of Report ────────────────────────\n");

        Files.writeString(outputPath, sb.toString(), StandardCharsets.UTF_8);
        LOGGER.info("Trip plan exported to " + outputPath);
    }
}
