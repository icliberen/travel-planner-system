package com.seng324.travelplanner.ui;

import com.seng324.travelplanner.model.City;
import com.seng324.travelplanner.model.WeatherState;
import com.seng324.travelplanner.observer.WeatherObserver;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/*
 * Observer Pattern - ConcreteObserver role
 * EN: The pie chart observes the weather publisher and redraws weather distribution percentages.
 * TR: Pie chart weather publisher'i izler ve hava dagilimi yuzdelerini tekrar cizer.
 */
public class WeatherPieChartPanel extends JPanel implements WeatherObserver {
    private final Map<WeatherState, Color> colors = new EnumMap<>(WeatherState.class);
    private volatile List<City> cities = new ArrayList<>();

    public WeatherPieChartPanel() {
        setPreferredSize(new Dimension(420, 260));
        setBackground(Color.WHITE);
        colors.put(WeatherState.SUNNY, new Color(244, 183, 64));
        colors.put(WeatherState.CLOUDY, new Color(132, 145, 166));
        colors.put(WeatherState.RAINY, new Color(68, 139, 202));
        colors.put(WeatherState.SNOWY, new Color(141, 205, 219));
    }

    @Override
    public void updateWeather(List<City> cities) {
        this.cities = new ArrayList<>(cities);
        if (SwingUtilities.isEventDispatchThread()) {
            repaint();
        } else {
            SwingUtilities.invokeLater(this::repaint);
        }
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        Graphics2D g2 = (Graphics2D) graphics.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(new Color(32, 40, 54));
        g2.drawString("Weather Distribution Pie Chart", 24, 20);

        if (cities.isEmpty()) {
            g2.drawString("No city data", 24, 58);
            g2.dispose();
            return;
        }

        Map<WeatherState, Integer> counts = new EnumMap<>(WeatherState.class);
        for (WeatherState state : WeatherState.values()) {
            counts.put(state, 0);
        }
        for (City city : cities) {
            counts.merge(city.getCurrentWeatherState(), 1, Integer::sum);
        }

        int diameter = Math.min(getWidth() / 2, getHeight() - 70);
        int x = 36;
        int y = 46;
        int startAngle = 0;

        for (WeatherState state : WeatherState.values()) {
            int count = counts.get(state);
            int angle = (int) Math.round(count * 360.0 / cities.size());
            g2.setColor(colors.get(state));
            g2.fillArc(x, y, diameter, diameter, startAngle, angle);
            startAngle += angle;
        }

        int legendX = x + diameter + 32;
        int legendY = y + 12;
        for (WeatherState state : WeatherState.values()) {
            int count = counts.get(state);
            double percent = count * 100.0 / cities.size();
            g2.setColor(colors.get(state));
            g2.fillRect(legendX, legendY - 10, 14, 14);
            g2.setColor(new Color(32, 40, 54));
            g2.drawString(state + " - " + String.format("%.0f%%", percent), legendX + 22, legendY + 2);
            legendY += 26;
        }

        g2.dispose();
    }
}
